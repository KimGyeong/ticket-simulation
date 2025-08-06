package com.kimgyeong.ticketingsimulation.ticket.application.port;

import com.kimgyeong.ticketingsimulation.global.exception.BusinessException;
import com.kimgyeong.ticketingsimulation.global.exception.ErrorCode;

public class TicketCannotRefundAfterEventStartException extends BusinessException {
	public TicketCannotRefundAfterEventStartException() {
		super(ErrorCode.TICKET_CANNOT_REFUND_AFTER_EVENT_START);
	}
}
