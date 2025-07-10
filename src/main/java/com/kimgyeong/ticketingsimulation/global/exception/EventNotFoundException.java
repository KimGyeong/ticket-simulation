package com.kimgyeong.ticketingsimulation.global.exception;

public class EventNotFoundException extends BusinessException {
	public EventNotFoundException() {
		super(ErrorCode.EVENT_NOT_FOUND);
	}
}
