package com.kimgyeong.ticketingsimulation.event.application.port.in;

import java.time.LocalDateTime;

public interface ReleaseSeatUseCase {
	void releaseExpiredSeats(LocalDateTime threshold);
}
