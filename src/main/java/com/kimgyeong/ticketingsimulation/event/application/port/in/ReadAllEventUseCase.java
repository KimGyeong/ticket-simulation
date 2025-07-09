package com.kimgyeong.ticketingsimulation.event.application.port.in;

import java.util.List;

import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

public interface ReadAllEventUseCase {
	List<Event> findAll();
}
