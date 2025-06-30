package com.kimgyeong.ticketingsimulation.event.application.port.in;

public interface HoldSeatUseCase {
	void holdSeat(Long seatId, Long userId);
}
