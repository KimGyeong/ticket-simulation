package com.kimgyeong.ticketingsimulation.ticket.adapter.in.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kimgyeong.ticketingsimulation.global.auth.CustomUserDetails;
import com.kimgyeong.ticketingsimulation.ticket.adapter.in.web.dto.PurchaseTicketRequest;
import com.kimgyeong.ticketingsimulation.ticket.application.port.in.PurchaseTicketUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/tickets")
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Slf4j
@Tag(name = "Ticket API", description = "티켓 API")
public class TicketController {
	private final PurchaseTicketUseCase purchaseTicketUseCase;

	@Operation(summary = "티켓 구매", description = "사용자가 점유한 좌석에 대해 티켓을 구매합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "티켓 구매 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "403", description = "권한 없음 또는 점유하지 않은 좌석"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@PostMapping
	public ResponseEntity<Void> purchaseTicket(@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody PurchaseTicketRequest request) {
		Long userId = userDetails.getUserId();

		log.info("purchase ticket user id: {}, event id: {}, seat id: {}", userId, request.eventId(), request.seatId());

		Long ticketId = purchaseTicketUseCase.purchase(userId, request.eventId(), request.seatId());

		return ResponseEntity.created(URI.create("/api/tickets/" + ticketId)).build();
	}
}
