package com.kimgyeong.ticketingsimulation.event.application.port.out;

import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

public interface EventRepositoryPort {
	Event save(Event event);
}
