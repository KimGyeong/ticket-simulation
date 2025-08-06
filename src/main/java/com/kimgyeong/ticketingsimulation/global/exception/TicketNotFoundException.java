package com.kimgyeong.ticketingsimulation.global.exception;

public class TicketNotFoundException extends BusinessException {
	public TicketNotFoundException() {
		super(ErrorCode.TICKET_NOT_FOUND);
	}
}
