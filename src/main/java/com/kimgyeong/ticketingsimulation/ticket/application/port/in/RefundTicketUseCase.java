package com.kimgyeong.ticketingsimulation.ticket.application.port.in;

public interface RefundTicketUseCase {
	void refund(Long ticketId, Long userId);
}
