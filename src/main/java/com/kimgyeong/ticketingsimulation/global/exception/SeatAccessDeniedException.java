package com.kimgyeong.ticketingsimulation.global.exception;

public class SeatAccessDeniedException extends BusinessException {
	public SeatAccessDeniedException() {
		super(ErrorCode.SEAT_ACCESS_DENIED);
	}
}
