package com.kimgyeong.ticketingsimulation.event.application.port.in;

public interface DeleteEventUseCase {
	void deleteById(Long eventId, Long userId);
}
