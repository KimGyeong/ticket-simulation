package com.kimgyeong.ticketingsimulation.ticket.domain.model;

public enum TicketStatus {
	PURCHASED,
	REFUNDED;

	public boolean isRefunded() {
		return this == REFUNDED;
	}
}
