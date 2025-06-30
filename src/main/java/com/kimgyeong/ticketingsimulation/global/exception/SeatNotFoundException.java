package com.kimgyeong.ticketingsimulation.global.exception;

public class SeatNotFoundException extends BusinessException {
	public SeatNotFoundException() {
		super(ErrorCode.SEAT_NOT_FOUND);
	}
}
