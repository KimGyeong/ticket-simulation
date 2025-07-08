package com.kimgyeong.ticketingsimulation.event.domain.factory;

import java.util.List;
import java.util.stream.IntStream;

import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.event.domain.model.SeatStatus;

public class SeatFactory {
	public static List<Seat> createSeats(Long eventId, int count) {
		return IntStream.range(0, count)
			.mapToObj(number -> new Seat(null, eventId, SeatStatus.AVAILABLE, number + 1, null, null))
			.toList();
	}
}
