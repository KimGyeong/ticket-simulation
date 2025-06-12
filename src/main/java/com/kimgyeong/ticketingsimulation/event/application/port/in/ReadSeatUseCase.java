package com.kimgyeong.ticketingsimulation.event.application.port.in;

import java.util.List;

import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;

public interface ReadSeatUseCase {
	List<Seat> getSeatsByEventId(Long eventId);
}
