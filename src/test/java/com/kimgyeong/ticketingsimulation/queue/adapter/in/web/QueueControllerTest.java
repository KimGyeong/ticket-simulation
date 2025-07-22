package com.kimgyeong.ticketingsimulation.queue.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.core.IsNull;
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
import com.kimgyeong.ticketingsimulation.queue.application.port.in.ReadQueueRankUseCase;

@WebMvcTest(controllers = QueueController.class)
class QueueControllerTest extends AbstractControllerTest {
	@MockitoBean
	private EnterQueueUseCase enterQueueUseCase;

	@MockitoBean
	private ReadQueueRankUseCase readQueueRankUseCase;

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
}