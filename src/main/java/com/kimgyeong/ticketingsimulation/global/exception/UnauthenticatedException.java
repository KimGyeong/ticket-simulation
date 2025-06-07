package com.kimgyeong.ticketingsimulation.global.exception;

public class UnauthenticatedException extends BusinessException {
	public UnauthenticatedException() {
		super(ErrorCode.UNAUTHENTICATED);
	}
}
