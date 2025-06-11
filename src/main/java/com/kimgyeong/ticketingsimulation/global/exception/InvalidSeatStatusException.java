package com.kimgyeong.ticketingsimulation.global.exception;

public class InvalidSeatStatusException extends BusinessException {
	public InvalidSeatStatusException() {
		super(ErrorCode.INVALID_SEAT_STATUS);
	}
}
