package com.kimgyeong.ticketingsimulation.event.adapter.in.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto.SeatResponses;
import com.kimgyeong.ticketingsimulation.event.application.port.in.HoldSeatUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.in.ReadSeatUseCase;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.global.auth.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/seats")
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Tag(name = "Seat API", description = "좌석 정보 API")
public class SeatController {
	private final ReadSeatUseCase readSeatUseCase;
	private final HoldSeatUseCase holdSeatUseCase;

	@Operation(summary = "좌석 정보 조회", description = "이벤트의 좌석을 전체 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
	})
	@GetMapping
	public ResponseEntity<SeatResponses> getSeatsByEventId(
		@RequestParam(name = "event-id", required = true) Long eventId) {
		log.info("Get Seats event id: {}", eventId);
		List<Seat> seats = readSeatUseCase.getSeatsByEventId(eventId);
		return ResponseEntity.ok(SeatResponses.from(seats));
	}

	@Operation(summary = "좌석 점유", description = "지정한 좌석을 현재 사용자로 점유합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "좌석 점유 성공"),
		@ApiResponse(responseCode = "400", description = "이미 점유되었거나 예약된 좌석"),
		@ApiResponse(responseCode = "404", description = "좌석을 찾을 수 없음"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@PostMapping("/{seatId}/hold")
	public ResponseEntity<Void> holdSeat(@PathVariable Long seatId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		log.info("Hold Seat seat id: {}, user id: {}", seatId, userDetails.getUserId());
		Long userId = userDetails.getUserId();
		holdSeatUseCase.holdSeat(seatId, userId);
		return ResponseEntity.ok().build();
	}
}
