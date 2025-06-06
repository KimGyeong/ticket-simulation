package com.kimgyeong.ticketingsimulation.global.exception;

public record ErrorResponse(String code, String message) {
	public static ErrorResponse from(ErrorCode errorCode) {
		return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
	}
}
