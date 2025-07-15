package com.kimgyeong.ticketingsimulation.global.exception;

public class EventModificationTimeExpiredException extends BusinessException {
	public EventModificationTimeExpiredException() {
		super(ErrorCode.INVALID_EVENT_UPDATE);
	}
}
