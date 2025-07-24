package com.kimgyeong.ticketingsimulation.queue.application.port.in;

public interface GrantQueueAccessUseCase {
	void grantAccess(Long userId, Long eventId);
}
