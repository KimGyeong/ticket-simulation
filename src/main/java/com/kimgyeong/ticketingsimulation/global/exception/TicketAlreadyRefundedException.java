package com.kimgyeong.ticketingsimulation.global.exception;

public class TicketAlreadyRefundedException extends BusinessException {
	public TicketAlreadyRefundedException() {
		super(ErrorCode.TICKET_ALREADY_REFUNDED);
	}
}
