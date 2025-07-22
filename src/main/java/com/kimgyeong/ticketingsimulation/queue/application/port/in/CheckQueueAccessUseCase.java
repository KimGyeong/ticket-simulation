package com.kimgyeong.ticketingsimulation.queue.application.port.in;

public interface CheckQueueAccessUseCase {
	boolean hasAccess(Long userId, Long eventId);
}
