package com.kimgyeong.ticketingsimulation.event.domain.factory;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.event.domain.model.SeatStatus;

class SeatFactoryTest {

	@Test
	void createSeats() {
		Long eventId = 500L;
		int count = 5;

		List<Seat> seats = SeatFactory.createSeats(eventId, count);

		assertThat(seats).hasSize(count);
		assertThat(seats)
			.extracting(Seat::eventId)
			.allMatch(id -> id.equals(eventId));

		assertThat(seats)
			.extracting(Seat::number)
			.containsExactly(1, 2, 3, 4, 5);

		assertThat(seats)
			.extracting(Seat::status)
			.allMatch(status -> status == SeatStatus.AVAILABLE);
	}
}