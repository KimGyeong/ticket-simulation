package com.kimgyeong.ticketingsimulation.event.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto.CreateEventRequest;
import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventCommand;
import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventUseCase;
import com.kimgyeong.ticketingsimulation.global.controller.AbstractControllerTest;
import com.kimgyeong.ticketingsimulation.global.security.annotation.WithMockCustomUser;

@WebMvcTest(EventController.class)
class EventControllerTest extends AbstractControllerTest {
	@MockitoBean
	private CreateEventUseCase createEventUseCase;

	@Test
	@WithMockCustomUser
	void createEvent_validRequest_returns200() throws Exception {
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
}