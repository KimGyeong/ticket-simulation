package com.kimgyeong.ticketingsimulation.event.application.port.in;

public interface CreateEventUseCase {
	Long createEvent(Long userId, CreateEventCommand command);
}
