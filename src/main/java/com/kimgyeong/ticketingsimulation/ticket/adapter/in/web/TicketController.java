package com.kimgyeong.ticketingsimulation.ticket.adapter.in.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kimgyeong.ticketingsimulation.global.auth.CustomUserDetails;
import com.kimgyeong.ticketingsimulation.ticket.adapter.in.web.dto.PurchaseTicketRequest;
import com.kimgyeong.ticketingsimulation.ticket.adapter.in.web.dto.TicketResponses;
import com.kimgyeong.ticketingsimulation.ticket.application.port.in.PurchaseTicketUseCase;
import com.kimgyeong.ticketingsimulation.ticket.application.port.in.ReadTicketUseCase;
import com.kimgyeong.ticketingsimulation.ticket.application.port.in.RefundTicketUseCase;

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
	private final ReadTicketUseCase readTicketUseCase;
	private final RefundTicketUseCase refundTicketUseCase;

	@Operation(summary = "티켓 구매", description = "사용자가 점유한 좌석에 대해 티켓을 구매합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "티켓 구매 성공"),
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

	@Operation(summary = "티켓 조회", description = "사용자가 보유한 티켓을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "404", description = "보유한 티켓의 좌석이 존재하지 않음"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("/me")
	public ResponseEntity<TicketResponses> findAllByUserId(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUserId();

		log.info("read tickets user id: {}", userId);

		return ResponseEntity.ok(TicketResponses.from(readTicketUseCase.findTicketsByUserId(userId)));
	}

	@Operation(summary = "티켓 환불", description = "사용자가 구매한 티켓을 환불합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "환불 성공"),
		@ApiResponse(responseCode = "400", description = "이미 환불된 티켓이거나 이벤트 시작 이후의 티켓은 환불할 수 없습니다."),
		@ApiResponse(responseCode = "403", description = "해당 티켓은 현재 사용자에게 권한이 없습니다."),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 티켓이거나 연결된 이벤트/좌석이 없습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@PostMapping("/{ticketId}/refund")
	public ResponseEntity<Void> refundTicket(@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long ticketId) {
		Long userId = userDetails.getUserId();
		log.info("refund ticket ticket id: {}, user id: {}", ticketId, userId);
		refundTicketUseCase.refund(ticketId, userId);
		return ResponseEntity.noContent().build();
	}
}
