package com.kimgyeong.ticketingsimulation.queue.adapter.in.web;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kimgyeong.ticketingsimulation.global.auth.CustomUserDetails;
import com.kimgyeong.ticketingsimulation.queue.adapter.in.web.dto.CheckQueueAccessResponse;
import com.kimgyeong.ticketingsimulation.queue.adapter.in.web.dto.EnterQueueRequest;
import com.kimgyeong.ticketingsimulation.queue.adapter.in.web.dto.QueueRankResponse;
import com.kimgyeong.ticketingsimulation.queue.application.port.in.CheckQueueAccessUseCase;
import com.kimgyeong.ticketingsimulation.queue.application.port.in.EnterQueueUseCase;
import com.kimgyeong.ticketingsimulation.queue.application.port.in.ReadQueueRankUseCase;
import com.kimgyeong.ticketingsimulation.queue.messaging.message.EnterQueueMessage;
import com.kimgyeong.ticketingsimulation.queue.messaging.producer.EnterQueueProducer;

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
	private final ReadQueueRankUseCase readQueueRankUseCase;
	private final CheckQueueAccessUseCase checkQueueAccessUseCase;
	private final EnterQueueProducer enterQueueProducer;
	private final Clock clock;

	@PostMapping("/enter")
	@Operation(summary = "대기열 입장")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "입장 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "400", description = "티켓팅 시작 시각 이전 요청"),
		@ApiResponse(responseCode = "400", description = "이벤트 시작 시각 이후 요청"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	public ResponseEntity<QueueRankResponse> enterQueue(@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody EnterQueueRequest enterQueueRequest) {
		Long userId = userDetails.getUserId();
		Long eventId = enterQueueRequest.eventId();
		LocalDateTime now = LocalDateTime.now(clock);

		log.info("enter queue user id: {}, event id: {}", userId, eventId);
		Long rank = enterQueueUseCase.enter(userId, eventId, now);

		EnterQueueMessage message = new EnterQueueMessage(userId, eventId, now);
		enterQueueProducer.send(message);

		return ResponseEntity.ok(QueueRankResponse.from(rank));
	}

	@GetMapping("/rank")
	@Operation(summary = "대기열 랭크 조회", description = "사용자의 현재 대기열 순서를 반환합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	public ResponseEntity<QueueRankResponse> getRank(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(value = "event-id", required = true) Long eventId) {
		Long userId = userDetails.getUserId();
		log.info("get rank user id: {}, event id: {}", userId, eventId);
		Long rank = readQueueRankUseCase.getRank(userId, eventId);
		return ResponseEntity.ok(QueueRankResponse.from(rank));
	}

	@GetMapping("/access")
	@Operation(summary = "대기열 입장 허용 여부 조회", description = "")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	public ResponseEntity<CheckQueueAccessResponse> checkAccess(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(value = "event-id", required = true) Long eventId
	) {
		Long userId = userDetails.getUserId();
		log.info("check access user id: {}, event id: {}", userId, eventId);
		boolean hasAccess = checkQueueAccessUseCase.hasAccess(userId, eventId);
		return ResponseEntity.ok(CheckQueueAccessResponse.from(hasAccess));
	}
}
