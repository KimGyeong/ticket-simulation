package com.kimgyeong.ticketingsimulation.queue.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.kimgyeong.ticketingsimulation.global.controller.AbstractControllerTest;
import com.kimgyeong.ticketingsimulation.global.exception.EventAlreadyStartedException;
import com.kimgyeong.ticketingsimulation.global.exception.TicketingNotOpenedException;
import com.kimgyeong.ticketingsimulation.global.security.annotation.WithMockCustomUser;
import com.kimgyeong.ticketingsimulation.queue.adapter.in.web.dto.EnterQueueRequest;
import com.kimgyeong.ticketingsimulation.queue.application.port.in.EnterQueueUseCase;

@WebMvcTest(controllers = QueueController.class)
class QueueControllerTest extends AbstractControllerTest {
	@MockitoBean
	private EnterQueueUseCase enterQueueUseCase;

	@Test
	@WithMockCustomUser
	void enterQueue_validRequest_returns200() throws Exception {
		EnterQueueRequest request = new EnterQueueRequest(100L);
		given(enterQueueUseCase.enter(anyLong(), anyLong()))
			.willReturn(0L);

		mockMvc.perform(post("/api/queue/enter")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.rank").value(0));
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
	void enterQueue_beforeTicketingStart_returns400() throws Exception {
		EnterQueueRequest request = new EnterQueueRequest(100L);
		given(enterQueueUseCase.enter(anyLong(), anyLong()))
			.willThrow(new TicketingNotOpenedException());

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
		given(enterQueueUseCase.enter(anyLong(), anyLong()))
			.willThrow(new EventAlreadyStartedException());

		mockMvc.perform(post("/api/queue/enter")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").exists());
	}
}