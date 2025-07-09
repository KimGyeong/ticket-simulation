package com.kimgyeong.ticketingsimulation.event.adapter.in.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto.CreateEventRequest;
import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventCommand;
import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Tag(name = "Event API", description = "이벤트 정보 API")
public class EventController {
	private final CreateEventUseCase createEventUseCase;

	@Operation(summary = "이벤트 생성", description = "새로운 이벤트를 생성하고 좌석을 자동으로 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "이벤트 생성 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@PostMapping
	public ResponseEntity<Void> createEvent(@Valid @RequestBody CreateEventRequest request) {
		CreateEventCommand command = request.toCommand();
		Long eventId = createEventUseCase.createEvent(command);
		return ResponseEntity.created(URI.create("/api/events/" + eventId)).build();
	}
}
