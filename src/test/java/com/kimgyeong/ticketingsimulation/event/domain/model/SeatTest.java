package com.kimgyeong.ticketingsimulation.event.domain.model;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.kimgyeong.ticketingsimulation.global.exception.InvalidSeatStatusException;
import com.kimgyeong.ticketingsimulation.global.exception.SeatAlreadyBookedException;
import com.kimgyeong.ticketingsimulation.global.exception.SeatAlreadyHeldException;

class SeatTest {
	Seat availableSeat = new Seat(1L, 1L, SeatStatus.AVAILABLE, 1, LocalDateTime.now());
	Seat temporaryHoldSeat = new Seat(2L, 1L, SeatStatus.TEMPORARY_HOLD, 1, LocalDateTime.now().minusSeconds(200));
	Seat temporaryHoldSeatNotHold = new Seat(2L, 1L, SeatStatus.TEMPORARY_HOLD, 1, null);
	Seat temporaryHoldSeatExpired = new Seat(2L, 1L, SeatStatus.TEMPORARY_HOLD, 1, LocalDateTime.now());
	Seat bookedSeat = new Seat(3L, 1L, SeatStatus.BOOKED, 1, LocalDateTime.now());

	@Test
	void isHoldExpired() {
		assertTrue(temporaryHoldSeat.isHoldExpired());
	}

	@Test
	void isHoldExpired_whenSeatAvailable() {
		assertFalse(availableSeat.isHoldExpired());
	}

	@Test
	void isHoldExpired_whenSeatBooked() {
		assertFalse(bookedSeat.isHoldExpired());
	}

	@Test
	void isHoldExpired_whenHeldAtNull() {
		assertFalse(temporaryHoldSeatNotHold.isHoldExpired());
	}

	@Test
	void isHoldExpired_whenNotExpired() {
		assertFalse(temporaryHoldSeatExpired.isHoldExpired());
	}

	@Test
	void hold() {
		assertThat(availableSeat.hold().status()).isEqualTo(SeatStatus.TEMPORARY_HOLD);
	}

	@Test
	void hold_whenSeatTemporaryHold() {
		assertThrows(SeatAlreadyHeldException.class, () -> temporaryHoldSeat.hold());
	}

	@Test
	void hold_whenSeatBooked() {
		assertThrows(SeatAlreadyBookedException.class, () -> bookedSeat.hold());
	}

	@Test
	void release() {
		assertThat(temporaryHoldSeat.release().status()).isEqualTo(SeatStatus.AVAILABLE);
	}

	@Test
	void release_whenSeatNotTemporaryHold() {
		assertThat(availableSeat.release().status()).isEqualTo(SeatStatus.AVAILABLE);
		assertThat(bookedSeat.release().status()).isEqualTo(SeatStatus.BOOKED);
	}

	@Test
	void book() {
		assertThat(temporaryHoldSeat.book().status()).isEqualTo(SeatStatus.BOOKED);
	}

	@Test
	void book_whenSeatAvailable() {
		assertThrows(InvalidSeatStatusException.class, () -> availableSeat.book());
	}

	@Test
	void book_whenSeatBooked() {
		assertThrows(SeatAlreadyBookedException.class, () -> bookedSeat.book());
	}
}