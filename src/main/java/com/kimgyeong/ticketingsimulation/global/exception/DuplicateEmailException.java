package com.kimgyeong.ticketingsimulation.global.exception;

public class DuplicateEmailException extends BusinessException {
	public DuplicateEmailException() {
		super(ErrorCode.DUPLICATE_EMAIL);
	}
}
