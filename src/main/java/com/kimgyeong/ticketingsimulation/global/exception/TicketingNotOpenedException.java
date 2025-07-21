package com.kimgyeong.ticketingsimulation.global.exception;

public class TicketingNotOpenedException extends BusinessException {
	public TicketingNotOpenedException() {
		super(ErrorCode.TICKETING_NOT_OPENED);
	}
}
