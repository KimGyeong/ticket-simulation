package com.kimgyeong.ticketingsimulation.event.domain.model;

import java.time.LocalDateTime;

import com.kimgyeong.ticketingsimulation.global.exception.InvalidSeatStatusException;
import com.kimgyeong.ticketingsimulation.global.exception.SeatAlreadyBookedException;
import com.kimgyeong.ticketingsimulation.global.exception.SeatAlreadyHeldException;

public record Seat(Long id, Long eventId, SeatStatus status, int number, LocalDateTime heldAt, Long heldUserId) {
	private static final int EXPIRE_TIME = 120;

	public boolean isHoldExpired() {
		return status == SeatStatus.TEMPORARY_HOLD &&
			heldAt != null &&
			heldAt.plusSeconds(EXPIRE_TIME).isBefore(LocalDateTime.now());
	}

	public Seat hold(Long userId) {
		return switch (status) {
			case AVAILABLE -> new Seat(id, eventId, SeatStatus.TEMPORARY_HOLD, number, LocalDateTime.now(), userId);
			case TEMPORARY_HOLD -> throw new SeatAlreadyHeldException();
			case BOOKED -> throw new SeatAlreadyBookedException();
		};
	}

	public Seat release() {
		return switch (status) {
			case TEMPORARY_HOLD -> new Seat(id, eventId, SeatStatus.AVAILABLE, number, null, null);
			case AVAILABLE, BOOKED -> this; // release 대상이 아님, 그대로 반환
		};
	}

	public Seat book() {
		return switch (status) {
			case TEMPORARY_HOLD -> new Seat(id, eventId, SeatStatus.BOOKED, number, null, heldUserId);
			case AVAILABLE -> throw new InvalidSeatStatusException();
			case BOOKED -> throw new SeatAlreadyBookedException();
		};
	}
}
