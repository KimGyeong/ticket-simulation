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
import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventCommand;
import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.in.ReadAllEventUseCase;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.global.controller.AbstractControllerTest;
import com.kimgyeong.ticketingsimulation.global.security.annotation.WithMockCustomUser;

@WebMvcTest(EventController.class)
class EventControllerTest extends AbstractControllerTest {
	@MockitoBean
	private CreateEventUseCase createEventUseCase;

	@MockitoBean
	private ReadAllEventUseCase readAllEventUseCase;

	@Test
	@WithMockCustomUser
	void createEvent_validRequest_returns201() throws Exception {
		given(createEventUseCase.createEvent(any(CreateEventCommand.class)))
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
			LocalDateTime.now().plusDays(1), 100);
		when(readAllEventUseCase.findAll()).thenReturn(List.of(event));

		mockMvc.perform(get("/api/events")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.eventResponses").isArray())
			.andExpect(jsonPath("$.eventResponses.length()").value(1))
			.andExpect(jsonPath("$.eventResponses[0].id").value(1L))
			.andExpect(jsonPath("$.eventResponses[0].title").value("테스트 이벤트"));
	}
}