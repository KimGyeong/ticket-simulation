package com.kimgyeong.ticketingsimulation.ticket.domain.model;

import java.time.LocalDateTime;

public record Ticket(
	Long id,
	Long userId,
	Long eventId,
	Long seatId,
	LocalDateTime purchasedAt,
	LocalDateTime refundedAt,
	TicketStatus status
) {
	public boolean isRefunded() {
		return this.status.isRefunded();
	}

	public Ticket refund(LocalDateTime refundedAt) {
		return new Ticket(
			this.id,
			this.userId,
			this.eventId,
			this.seatId,
			this.purchasedAt,
			refundedAt,
			TicketStatus.REFUNDED
		);
	}
}
