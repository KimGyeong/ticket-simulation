package com.kimgyeong.ticketingsimulation.global.exception;

public class NotInAccessQueueException extends BusinessException {
	public NotInAccessQueueException() {
		super(ErrorCode.NOT_IN_ACCESS_QUEUE);
	}
}
