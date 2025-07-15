package com.kimgyeong.ticketingsimulation.global.exception;

public class EventAccessDeniedException extends BusinessException {
	public EventAccessDeniedException() {
		super(ErrorCode.EVENT_ACCESS_DENIED);
	}
}
