package com.kimgyeong.ticketingsimulation.event.application.port.out;

import java.util.List;
import java.util.Optional;

import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

public interface EventRepositoryPort {
	Event save(Event event);

	List<Event> findAll();

	Optional<Event> findById(Long id);

	void deleteById(Long id);
}
