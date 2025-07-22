package com.kimgyeong.ticketingsimulation.queue.application.port.in;

public interface ReadQueueRankUseCase {
	Long getRank(Long userId, Long eventId);
}
