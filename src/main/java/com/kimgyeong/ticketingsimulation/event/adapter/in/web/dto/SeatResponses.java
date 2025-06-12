package com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto;

import java.util.List;

import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;

public record SeatResponses(List<SeatResponse> seatResponses) {

	public static SeatResponses from(List<Seat> seats) {
		List<SeatResponse> seatResponses = seats.stream()
			.map(SeatResponse::from)
			.toList();
		return new SeatResponses(seatResponses);
	}
}
