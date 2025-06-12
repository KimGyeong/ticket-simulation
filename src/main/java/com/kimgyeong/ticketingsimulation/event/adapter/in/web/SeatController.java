package com.kimgyeong.ticketingsimulation.event.adapter.in.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto.SeatResponses;
import com.kimgyeong.ticketingsimulation.event.application.port.in.ReadSeatUseCase;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Tag(name = "Seat API", description = "좌석 정보 API")
public class SeatController {
	private final ReadSeatUseCase readSeatUseCase;

	@Operation(summary = "좌석 정보 조회", description = "이벤트의 좌석을 전체 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
	})
	@GetMapping
	public ResponseEntity<SeatResponses> getSeatsByEventId(
		@RequestParam(name = "event-id", required = true) Long eventId) {
		List<Seat> seats = readSeatUseCase.getSeatsByEventId(eventId);
		return ResponseEntity.ok(SeatResponses.from(seats));
	}

}
