package com.kimgyeong.ticketingsimulation.event.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto.CreateEventRequest;
import com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto.UpdateEventRequest;
import com.kimgyeong.ticketingsimulation.event.application.model.EventDetailResult;
import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventCommand;
import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.in.ReadAllEventUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.in.ReadEventUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.in.UpdateEventUseCase;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.global.controller.AbstractControllerTest;
import com.kimgyeong.ticketingsimulation.global.security.annotation.WithMockCustomUser;

@WebMvcTest(EventController.class)
class EventControllerTest extends AbstractControllerTest {
	@MockitoBean
	private CreateEventUseCase createEventUseCase;

	@MockitoBean
	private ReadAllEventUseCase readAllEventUseCase;

	@MockitoBean
	private ReadEventUseCase readEventUseCase;

	@MockitoBean
	private UpdateEventUseCase updateEventUseCase;

	@Test
	@WithMockCustomUser
	void createEvent_validRequest_returns201() throws Exception {
		given(createEventUseCase.createEvent(anyLong(), any(CreateEventCommand.class)))
			.willReturn(1L);

		CreateEventRequest request = new CreateEventRequest(
			"테스트 이벤트",
			"테스트 설명",
			"https://image.com/poster.jpg",
			LocalDateTime.of(2025, 8, 1, 18, 0),
			LocalDateTime.of(2025, 8, 2, 20, 0),
			100
		);

		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/events/1"));
	}

	@Test
	@WithMockCustomUser
	void createEvent_invalidRequest_returns400() throws Exception {
		CreateEventRequest request = new CreateEventRequest(
			"",
			"설명",
			"",
			LocalDateTime.of(2025, 8, 1, 18, 0),
			LocalDateTime.of(2025, 8, 2, 20, 0),
			0
		);

		// when & then
		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockCustomUser
	void findAllEvent_validRequest_returns200() throws Exception {
		Event event = new Event(1L, "테스트 이벤트", "테스트 설명", "테스트 이미지", LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(1), 100, 1L);
		when(readAllEventUseCase.findAll()).thenReturn(List.of(event));

		mockMvc.perform(get("/api/events")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.eventResponses").isArray())
			.andExpect(jsonPath("$.eventResponses.length()").value(1))
			.andExpect(jsonPath("$.eventResponses[0].id").value(1L))
			.andExpect(jsonPath("$.eventResponses[0].title").value("테스트 이벤트"));
	}

	@Test
	@WithMockCustomUser
	void findEventById_validRequest_returns200() throws Exception {
		Event event = new Event(1L, "테스트 이벤트", "테스트 설명", "테스트 이미지", LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(1), 100, 1L);
		EventDetailResult result = new EventDetailResult(event, 5L);
		when(readEventUseCase.findById(anyLong())).thenReturn(result);

		mockMvc.perform(get("/api/events/" + event.id())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(event.id()))
			.andExpect(jsonPath("$.title").value(event.title()))
			.andExpect(jsonPath("$.availableSeatCount").value(result.availableSeatCount()));
	}

	@Test
	@WithMockCustomUser
	void updateEvent_validRequest_returns200() throws Exception {
		UpdateEventRequest updateEventRequest = new UpdateEventRequest("변경 테스트", "변경 설명", "변경 이미지",
			LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(1), 100);

		Event result = new Event(1L, "테스트 이벤트", "테스트 설명", "테스트 이미지", LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(1), 100, 1L);

		when(updateEventUseCase.updateEvent(anyLong(), anyLong(), any())).thenReturn(result);

		mockMvc.perform(patch("/api/events/" + result.id())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateEventRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(result.id()))
			.andExpect(jsonPath("$.title").value(result.title()));
	}

}