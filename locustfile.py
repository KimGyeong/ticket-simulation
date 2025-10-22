import os
import time
import random
from datetime import datetime, timedelta
from typing import Any, Dict, List, Optional

import jwt
from locust import HttpUser, task, between
from locust.exception import StopUser

# Environment variables / defaults
JWT_SECRET = os.getenv(
    "JWT_SECRET",
    # Must match application.properties: jwt.secret
    "7033d59ff10ae05ca43833f94f72d6bd80a9c0c35e0d87f9e7795d3c59a7d6189d449265d15c7cae4ae0d3678a0a82cc938a50d9c8af5afcc5b64f3a731c5f85",
)
TARGET_EVENT_ID = os.getenv("EVENT_ID")  # optional
ACCESS_POLL_SEC_MIN = float(os.getenv("ACCESS_POLL_SEC_MIN", "0.5"))
ACCESS_POLL_SEC_MAX = float(os.getenv("ACCESS_POLL_SEC_MAX", "1.0"))
ACTION_DELAY_SEC_MIN = float(os.getenv("ACTION_DELAY_SEC_MIN", "0.5"))
ACTION_DELAY_SEC_MAX = float(os.getenv("ACTION_DELAY_SEC_MAX", "2.0"))
MAX_ACCESS_WAIT_SEC = float(os.getenv("MAX_ACCESS_WAIT_SEC", "600"))  # 10 minutes safety cap


def parse_time(value: str) -> datetime:
    # Expecting Java LocalDateTime string, e.g. "2025-01-01T12:34:56"
    return datetime.fromisoformat(value)


def generate_user_token(user_id: int, email: str) -> str:
    now = datetime.utcnow()
    payload = {
        "sub": email,
        "userId": user_id,
        "roles": ["ROLE_USER"],
        "iat": int(now.timestamp()),
        "exp": int((now + timedelta(hours=1)).timestamp()),
    }
    return jwt.encode(payload, JWT_SECRET, algorithm="HS256")


class TicketingUser(HttpUser):
    # Using between for a tiny think time between full flows; main timing is manual sleeps
    wait_time = between(0.1, 0.5)

    def on_start(self) -> None:
        # 1) create pseudo user identity and JWT
        self.user_id = random.randint(10_000_000, 99_999_999)
        self.email = f"user{self.user_id}@example.com"
        token = generate_user_token(self.user_id, self.email)
        self.client.headers.update({"Authorization": f"Bearer {token}"})

        # 2) fetch events and select target event (nearest future ticketing or specific via env)
        resp = self.client.get("/api/events", name="GET /api/events")
        if resp.status_code != 200:
            # Can't operate without events
            raise StopUser("Failed to fetch events")
        body = resp.json() or {}
        events: List[Dict[str, Any]] = body.get("eventResponses", [])
        if not events:
            raise StopUser("No events available")

        now = datetime.now()
        target: Optional[Dict[str, Any]] = None
        if TARGET_EVENT_ID:
            target = next((e for e in events if str(e.get("id")) == str(TARGET_EVENT_ID)), None)
            if target is None:
                raise StopUser(f"EVENT_ID {TARGET_EVENT_ID} not found")
        else:
            future = [e for e in events if parse_time(e["ticketingStartAt"]) >= now]
            if future:
                target = sorted(future, key=lambda e: parse_time(e["ticketingStartAt"]))[0]
            else:
                target = events[0]

        self.event_id = target["id"]
        self.ticketing_start_at = parse_time(target["ticketingStartAt"])  # naive dt okay for relative sleeps

    @task
    def run_ticketing_flow(self) -> None:
        # 3) wait until ticketing start time (synchronize burst)
        now = datetime.now()
        wait_seconds = (self.ticketing_start_at - now).total_seconds()
        if wait_seconds > 0:
            time.sleep(wait_seconds)

        # 4) enter queue
        enter_payload = {"eventId": self.event_id}
        r = self.client.post("/api/queue/enter", json=enter_payload, name="POST /api/queue/enter")
        if r.status_code >= 400:
            # minimal retry after tiny backoff (open-edge contention)
            time.sleep(0.3)
            r = self.client.post("/api/queue/enter", json=enter_payload, name="POST /api/queue/enter(retry)")
        if r.status_code >= 400:
            # give up this iteration; next task run may try again
            return

        # 5) poll for access grant within a cap
        has_access = False
        poll_start = time.time()
        while not has_access and (time.time() - poll_start) < MAX_ACCESS_WAIT_SEC:
            ar = self.client.get(
                "/api/queue/access",
                params={"event-id": self.event_id},
                name="GET /api/queue/access",
            )
            if ar.status_code == 200:
                try:
                    has_access = bool((ar.json() or {}).get("hasAccess"))
                except Exception:
                    has_access = False
            if not has_access:
                time.sleep(random.uniform(ACCESS_POLL_SEC_MIN, ACCESS_POLL_SEC_MAX))
        if not has_access:
            # timed out waiting for access; let next loop try again
            return

        # 6) fetch seats
        seats_resp = self.client.get(
            "/api/seats", params={"event-id": self.event_id}, name="GET /api/seats"
        )
        if seats_resp.status_code != 200:
            return
        seats_body = seats_resp.json() or {}
        seats: List[Dict[str, Any]] = seats_body.get("seatResponses", [])
        available = [s for s in seats if s.get("status") == "AVAILABLE"]
        if not available:
            return

        # 7) random delay then try hold seat; on failure, try others
        held_seat_id: Optional[int] = None
        candidates = available.copy()
        random.shuffle(candidates)
        while candidates and held_seat_id is None:
            time.sleep(random.uniform(ACTION_DELAY_SEC_MIN, ACTION_DELAY_SEC_MAX))
            seat = candidates.pop()
            hold = self.client.post(
                f"/api/seats/{seat['id']}/hold",
                name="POST /api/seats/:id/hold",
            )
            if hold.status_code == 200:
                held_seat_id = seat["id"]
            else:
                # try another seat
                continue

        if held_seat_id is None:
            return

        # 8) random delay then purchase
        time.sleep(random.uniform(ACTION_DELAY_SEC_MIN, ACTION_DELAY_SEC_MAX))
        buy_payload = {"eventId": self.event_id, "seatId": held_seat_id}
        buy = self.client.post("/api/tickets", json=buy_payload, name="POST /api/tickets")
        # Expect 201; let Locust record results normally


# Notes for operators:
# - Start server and redis (docker-compose up) and then run Locust:
#   JWT_SECRET must match application.properties jwt.secret
#   export JWT_SECRET=...; locust -f locustfile.py --host http://localhost:8080 -u 1000 -r 200
#   Optionally pin an event: EVENT_ID=1 locust -f locustfile.py --host http://localhost:8080 -u 1000 -r 200
