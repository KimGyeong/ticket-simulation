package com.kimgyeong.ticketingsimulation.global.exception;

public class EventAlreadyStartedException extends BusinessException {
	public EventAlreadyStartedException() {
		super(ErrorCode.EVENT_ALREADY_STARTED);
	}
}
