package com.kimgyeong.ticketingsimulation.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
		ErrorCode code = e.getErrorCode();
		return ResponseEntity.status(code.getStatus())
			.body(ErrorResponse.from(code));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
		String message = e.getBindingResult().getFieldErrors().stream()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.findFirst()
			.orElse("잘못된 요청입니다.");

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse("VALIDATION_ERROR", message));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
		return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
			.body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
	}
}
