package com.kimgyeong.ticketingsimulation.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	DUPLICATE_EMAIL("E001", "이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),
	USER_NOT_FOUND("E002", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	INVALID_CREDENTIALS("E003", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
	UNAUTHENTICATED("E004", "접근할 수 없는 데이터입니다.", HttpStatus.UNAUTHORIZED),
	INTERNAL_SERVER_ERROR("E999", "서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

	private final String code;
	private final String message;
	private final HttpStatus status;

	ErrorCode(String code, String message, HttpStatus status) {
		this.code = code;
		this.message = message;
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
