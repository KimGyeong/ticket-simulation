package com.kimgyeong.ticketingsimulation.queue.application.port.in;

public interface EnterQueueUseCase {
	Long enter(Long userId, Long eventId);
}
