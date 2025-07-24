package com.kimgyeong.ticketingsimulation.queue.application.port.in;

import java.time.LocalDateTime;

public interface EnterQueueUseCase {
	Long enter(Long userId, Long eventId, LocalDateTime now);
}
