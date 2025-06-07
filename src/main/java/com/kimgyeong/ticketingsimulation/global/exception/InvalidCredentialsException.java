package com.kimgyeong.ticketingsimulation.global.exception;

public class InvalidCredentialsException extends BusinessException {
	public InvalidCredentialsException() {
		super(ErrorCode.INVALID_CREDENTIALS);
	}
}
