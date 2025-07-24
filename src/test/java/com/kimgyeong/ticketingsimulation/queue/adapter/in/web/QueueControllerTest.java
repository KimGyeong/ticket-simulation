package com.kimgyeong.ticketingsimulation.queue.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.kimgyeong.ticketingsimulation.global.controller.AbstractControllerTest;
import com.kimgyeong.ticketingsimulation.global.exception.EventAlreadyStartedException;
import com.kimgyeong.ticketingsimulation.global.exception.TicketingNotOpenedException;
import com.kimgyeong.ticketingsimulation.global.security.annotation.WithMockCustomUser;
import com.kimgyeong.ticketingsimulation.queue.adapter.in.web.dto.EnterQueueRequest;
import com.kimgyeong.ticketingsimulation.queue.application.port.in.CheckQueueAccessUseCase;
import com.kimgyeong.ticketingsimulation.queue.application.port.in.EnterQueueUseCase;
import com.kimgyeong.ticketingsimulation.queue.application.port.in.ReadQueueRankUseCase;
import com.kimgyeong.ticketingsimulation.queue.messaging.producer.EnterQueueProducer;

@WebMvcTest(controllers = QueueController.class)
class QueueControllerTest extends AbstractControllerTest {
	@MockitoBean
	private EnterQueueProducer enterQueueProducer;

	@MockitoBean
	private EnterQueueUseCase enterQueueUseCase;

	@MockitoBean
	private ReadQueueRankUseCase readQueueRankUseCase;

	@MockitoBean
	private CheckQueueAccessUseCase checkQueueAccessUseCase;
	@Autowired
	private Clock clock;

	@Test
	@WithMockCustomUser
	void enterQueue_validRequest_returns200() throws Exception {
		EnterQueueRequest request = new EnterQueueRequest(100L);
		given(enterQueueUseCase.enter(anyLong(), anyLong(), any(LocalDateTime.class)))
			.willReturn(0L);

		mockMvc.perform(post("/api/queue/enter")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.rank").value(0));

		verify(enterQueueProducer).send(any());
	}

	@Test
	@WithMockCustomUser
	void enterQueue_beforeTicketingStart_returns400() throws Exception {
		EnterQueueRequest request = new EnterQueueRequest(100L);
		given(enterQueueUseCase.enter(anyLong(), anyLong(), any(LocalDateTime.class)))
			.willThrow(new TicketingNotOpenedException());

		mockMvc.perform(post("/api/queue/enter")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").exists());
	}

	@Test
	@WithMockCustomUser
	void enterQueue_invalidRequest_returns400() throws Exception {
		EnterQueueRequest request = new EnterQueueRequest(null); // @NotBlank 실패 유도

		mockMvc.perform(post("/api/queue/enter")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").exists());
	}

	@Test
	@WithMockCustomUser
	void enterQueue_afterEventStart_returns400() throws Exception {
		EnterQueueRequest request = new EnterQueueRequest(100L);
		given(enterQueueUseCase.enter(anyLong(), anyLong(), any(LocalDateTime.class)))
			.willThrow(new EventAlreadyStartedException());

		mockMvc.perform(post("/api/queue/enter")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").exists());
	}

	@TestConfiguration
	static class ClockTestConfig {
		@Bean
		public Clock clock() {
			return Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("UTC"));
		}
	}

	@Test
	@WithMockCustomUser
	void getRank_validRequest_returns200() throws Exception {
		given(readQueueRankUseCase.getRank(anyLong(), anyLong())).willReturn(1L);

		mockMvc.perform(get("/api/queue/rank")
				.param("event-id", "1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.rank").value(1));
	}

	@Test
	@WithMockCustomUser
	void getRank_NotInQueue_returns200AndNull() throws Exception {
		given(readQueueRankUseCase.getRank(anyLong(), anyLong())).willReturn(null);

		mockMvc.perform(get("/api/queue/rank")
				.param("event-id", "1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.rank").value(IsNull.nullValue()));
	}

	@Test
	@WithMockCustomUser
	void checkAccess_validRequest_returns200_true() throws Exception {
		Long userId = 1L;
		Long eventId = 100L;

		given(checkQueueAccessUseCase.hasAccess(userId, eventId)).willReturn(true);

		mockMvc.perform(get("/api/queue/access")
				.param("event-id", eventId.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.hasAccess").value(true));
	}

	@Test
	@WithMockCustomUser
	void checkAccess_validRequest_returns200_false() throws Exception {
		Long userId = 1L;
		Long eventId = 100L;

		given(checkQueueAccessUseCase.hasAccess(userId, eventId)).willReturn(false);

		mockMvc.perform(get("/api/queue/access")
				.param("event-id", eventId.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.hasAccess").value(false));
	}
}