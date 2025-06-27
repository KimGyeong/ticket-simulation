package com.kimgyeong.ticketingsimulation.global.exception;

public class LockAcquisitionException extends BusinessException {
	public LockAcquisitionException() {
		super(ErrorCode.LOCK_ACQUISITION);
	}
}
