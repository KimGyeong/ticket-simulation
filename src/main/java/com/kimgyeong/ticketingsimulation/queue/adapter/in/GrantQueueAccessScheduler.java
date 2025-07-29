package com.kimgyeong.ticketingsimulation.queue.adapter.in;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.queue.application.port.out.QueueRepositoryPort;
import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class GrantQueueAccessScheduler {
	public static final int MAX_ALLOWED_USER_COUNT = 1000;
	private final EventRepositoryPort eventRepositoryPort;
	private final QueueRepositoryPort queueRepositoryPort;
	private final Clock clock;

	@Scheduled(fixedDelay = 3000)
	public void grantQueueAccess() {
		LocalDateTime now = LocalDateTime.now(clock);
		List<Event> events = eventRepositoryPort.findEventsInTicketingPeriod(now);

		for (Event event : events) {
			Long eventId = event.id();

			int currentAccessUserCount = queueRepositoryPort.countGrantedUsers(eventId);

			int toCurrentAccessUserCount = MAX_ALLOWED_USER_COUNT - currentAccessUserCount;

			if (toCurrentAccessUserCount <= 0) {
				continue;
			}

			List<QueueEntry> topEntries = queueRepositoryPort.getTopEntries(eventId, toCurrentAccessUserCount);

			for (QueueEntry topEntry : topEntries) {
				queueRepositoryPort.grantAccess(topEntry);
			}
		}
	}
}
