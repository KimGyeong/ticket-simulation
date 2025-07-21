package com.kimgyeong.ticketingsimulation.queue.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kimgyeong.ticketingsimulation.global.auth.CustomUserDetails;
import com.kimgyeong.ticketingsimulation.queue.adapter.in.web.dto.EnterQueueRequest;
import com.kimgyeong.ticketingsimulation.queue.adapter.in.web.dto.EnterQueueResponse;
import com.kimgyeong.ticketingsimulation.queue.application.port.in.EnterQueueUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@RequestMapping("/api/queue")
@Tag(name = "Queue API", description = "대기열 관련 API")
public class QueueController {
	private final EnterQueueUseCase enterQueueUseCase;

	@PostMapping("/enter")
	@Operation(summary = "대기열 입장")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "입장 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "400", description = "티켓팅 시작 시각 이전 요청"),
		@ApiResponse(responseCode = "400", description = "이벤트 시작 시각 이후 요청"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	public ResponseEntity<EnterQueueResponse> enterQueue(@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody EnterQueueRequest enterQueueRequest) {
		Long userId = userDetails.getUserId();
		Long eventId = enterQueueRequest.eventId();
		log.info("enter queue user id: {}, event id: {}", userId, eventId);
		Long rank = enterQueueUseCase.enter(userId, eventId);
		return ResponseEntity.ok(EnterQueueResponse.from(rank));
	}
}
