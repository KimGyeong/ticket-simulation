package com.kimgyeong.ticketingsimulation.ticket.application.port.in;

public interface PurchaseTicketUseCase {
	Long purchase(Long userId, Long eventId, Long seatId);
}
