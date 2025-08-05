package com.kimgyeong.ticketingsimulation.ticket.adapter.in.web.dto;

import java.time.LocalDateTime;

import com.kimgyeong.ticketingsimulation.ticket.application.model.TicketResult;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.TicketStatus;

public record TicketResponse(
	Long id,
	Long userId,
	Long eventId,
	int seatNumber,
	LocalDateTime purchasedAt,
	LocalDateTime refundedAt,
	TicketStatus status
) {
	public static TicketResponse from(TicketResult ticketResult) {
		Ticket ticket = ticketResult.ticket();
		return new TicketResponse(
			ticket.id(),
			ticket.userId(),
			ticket.eventId(),
			ticketResult.seatNumber(),
			ticket.purchasedAt(),
			ticket.refundedAt(),
			ticket.status()
		);
	}
}
