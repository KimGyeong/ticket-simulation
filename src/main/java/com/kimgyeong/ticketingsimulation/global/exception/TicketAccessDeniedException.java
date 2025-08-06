package com.kimgyeong.ticketingsimulation.global.exception;

public class TicketAccessDeniedException extends BusinessException {
	public TicketAccessDeniedException() {
		super(ErrorCode.TICKET_ACCESS_DENIED);
	}
}
