package com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto;

import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;

public record SeatResponse(Long id, int number, String status) {
	public static SeatResponse from(Seat seat) {
		return new SeatResponse(seat.id(), seat.number(), seat.status().name());
	}
}
