package com.kimgyeong.ticketingsimulation.event.adapter.in.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto.CreateEventRequest;
import com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto.DetailEventResponse;
import com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto.EventResponse;
import com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto.EventResponses;
import com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto.UpdateEventRequest;
import com.kimgyeong.ticketingsimulation.event.application.model.EventDetailResult;
import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventCommand;
import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.in.DeleteEventUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.in.ReadAllEventUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.in.ReadEventUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.in.UpdateEventCommand;
import com.kimgyeong.ticketingsimulation.event.application.port.in.UpdateEventUseCase;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.global.auth.CustomUserDetails;

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
	private final ReadAllEventUseCase readAllEventUseCase;
	private final ReadEventUseCase readEventUseCase;
	private final UpdateEventUseCase updateEventUseCase;
	private final DeleteEventUseCase deleteEventUseCase;

	@Operation(summary = "이벤트 생성", description = "새로운 이벤트를 생성하고 좌석을 자동으로 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "이벤트 생성 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@PostMapping
	public ResponseEntity<Void> createEvent(@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody CreateEventRequest request) {
		CreateEventCommand command = request.toCommand();
		Long eventId = createEventUseCase.createEvent(userDetails.getUserId(), command);
		return ResponseEntity.created(URI.create("/api/events/" + eventId)).build();
	}

	@Operation(summary = "이벤트 목록 조회", description = "이벤트 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이벤트 목록 조회 성공"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping
	public ResponseEntity<EventResponses> findAllEvent() {
		List<Event> allEvents = readAllEventUseCase.findAll();
		return ResponseEntity.ok(EventResponses.from(allEvents));
	}

	@Operation(summary = "이벤트 상세 조회", description = "이벤트 상세 데이터를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이벤트 조회 성공"),
		@ApiResponse(responseCode = "404", description = "이벤트 존재하지 않음"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("/{eventId}")
	public ResponseEntity<DetailEventResponse> findEventById(@PathVariable Long eventId) {
		EventDetailResult result = readEventUseCase.findById(eventId);
		return ResponseEntity.ok(DetailEventResponse.from(result));
	}

	@Operation(summary = "이벤트 수정", description = "이벤트 데이터를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이벤트 수정 성공"),
		@ApiResponse(responseCode = "400", description = "이벤트 수정 시각 아님"),
		@ApiResponse(responseCode = "400", description = "이벤트 수정 데이터 잘못됨"),
		@ApiResponse(responseCode = "401", description = "이벤트 접근 불가"),
		@ApiResponse(responseCode = "404", description = "이벤트 존재하지 않음"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@PatchMapping("/{eventId}")
	public ResponseEntity<EventResponse> updateEvent(@AuthenticationPrincipal CustomUserDetails userDetail,
		@PathVariable Long eventId, @RequestBody @Valid UpdateEventRequest updateEventRequest) {
		UpdateEventCommand command = updateEventRequest.toCommand();

		Event updatedEvent = updateEventUseCase.updateEvent(userDetail.getUserId(), eventId, command);

		return ResponseEntity.ok(EventResponse.from(updatedEvent));
	}

	@Operation(summary = "이벤트 삭제", description = "이벤트 데이터를 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "이벤트 삭제 성공"),
		@ApiResponse(responseCode = "401", description = "이벤트 접근 불가"),
		@ApiResponse(responseCode = "404", description = "이벤트 존재하지 않음"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@DeleteMapping("/{eventId}")
	public ResponseEntity<Void> deleteEvent(@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long eventId) {
		deleteEventUseCase.deleteById(eventId, userDetails.getUserId());

		return ResponseEntity.noContent().build();
	}
}
