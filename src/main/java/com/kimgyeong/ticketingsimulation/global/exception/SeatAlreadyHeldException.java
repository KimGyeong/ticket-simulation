package com.kimgyeong.ticketingsimulation.global.exception;

public class SeatAlreadyHeldException extends BusinessException {
	public SeatAlreadyHeldException() {
		super(ErrorCode.SEAT_ALREADY_HELD);
	}
}
