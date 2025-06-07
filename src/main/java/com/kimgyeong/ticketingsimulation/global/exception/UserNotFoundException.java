package com.kimgyeong.ticketingsimulation.global.exception;

public class UserNotFoundException extends BusinessException {
	public UserNotFoundException() {
		super(ErrorCode.USER_NOT_FOUND);
	}
}
