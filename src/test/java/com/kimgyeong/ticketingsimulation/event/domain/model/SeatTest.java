package com.kimgyeong.ticketingsimulation.event.domain.model;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.kimgyeong.ticketingsimulation.global.exception.InvalidSeatStatusException;
import com.kimgyeong.ticketingsimulation.global.exception.SeatAlreadyBookedException;
import com.kimgyeong.ticketingsimulation.global.exception.SeatAlreadyHeldException;

class SeatTest {
	Seat availableSeat = new Seat(1L, 1L, SeatStatus.AVAILABLE, 1, LocalDateTime.now(), null);
	Seat temporaryHoldSeat = new Seat(2L, 1L, SeatStatus.TEMPORARY_HOLD, 1, LocalDateTime.now().minusSeconds(200), 1L);
	Seat temporaryHoldSeatNotHold = new Seat(2L, 1L, SeatStatus.TEMPORARY_HOLD, 1, null, null);
	Seat temporaryHoldSeatExpired = new Seat(2L, 1L, SeatStatus.TEMPORARY_HOLD, 1, LocalDateTime.now(), 1L);
	Seat bookedSeat = new Seat(3L, 1L, SeatStatus.BOOKED, 1, LocalDateTime.now(), 1L);

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
		assertThat(availableSeat.hold(1L).status()).isEqualTo(SeatStatus.TEMPORARY_HOLD);
	}

	@Test
	void hold_whenSeatTemporaryHold() {
		assertThrows(SeatAlreadyHeldException.class, () -> temporaryHoldSeat.hold(1L));
	}

	@Test
	void hold_whenSeatBooked() {
		assertThrows(SeatAlreadyBookedException.class, () -> bookedSeat.hold(1L));
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