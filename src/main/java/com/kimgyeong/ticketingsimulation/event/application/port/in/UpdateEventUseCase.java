package com.kimgyeong.ticketingsimulation.event.application.port.in;

import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

public interface UpdateEventUseCase {
	Event updateEvent(Long userId, Long eventId, UpdateEventCommand updateEventCommand);
}
