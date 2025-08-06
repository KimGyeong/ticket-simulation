package com.kimgyeong.ticketingsimulation.ticket.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.kimgyeong.ticketingsimulation.global.controller.AbstractControllerTest;
import com.kimgyeong.ticketingsimulation.global.exception.TicketAccessDeniedException;
import com.kimgyeong.ticketingsimulation.global.exception.TicketAlreadyRefundedException;
import com.kimgyeong.ticketingsimulation.global.exception.TicketNotFoundException;
import com.kimgyeong.ticketingsimulation.global.security.annotation.WithMockCustomUser;
import com.kimgyeong.ticketingsimulation.ticket.adapter.in.web.dto.PurchaseTicketRequest;
import com.kimgyeong.ticketingsimulation.ticket.application.model.TicketResult;
import com.kimgyeong.ticketingsimulation.ticket.application.port.TicketCannotRefundAfterEventStartException;
import com.kimgyeong.ticketingsimulation.ticket.application.port.in.PurchaseTicketUseCase;
import com.kimgyeong.ticketingsimulation.ticket.application.port.in.ReadTicketUseCase;
import com.kimgyeong.ticketingsimulation.ticket.application.port.in.RefundTicketUseCase;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.TicketStatus;

@WebMvcTest(TicketController.class)
class TicketControllerTest extends AbstractControllerTest {
	@MockitoBean
	private PurchaseTicketUseCase purchaseTicketUseCase;

	@MockitoBean
	private ReadTicketUseCase readTicketUseCase;

	@MockitoBean
	private RefundTicketUseCase refundTicketUseCase;

	@Test
	@WithMockCustomUser
	void purchaseTicket_validRequest_returns201() throws Exception {
		given(purchaseTicketUseCase.purchase(anyLong(), anyLong(), anyLong())).willReturn(1L);

		PurchaseTicketRequest purchaseTicketRequest = new PurchaseTicketRequest(10L, 100L);

		mockMvc.perform(post("/api/tickets")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(purchaseTicketRequest)))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/tickets/1"));
	}

	@Test
	@WithMockCustomUser
	void findAllByUserId_validRequest_returns200() throws Exception {
		Ticket ticket1 = new Ticket(
			1L, 1L, 100L, 1000L,
			LocalDateTime.of(2025, 8, 1, 12, 0),
			null,
			TicketStatus.PURCHASED
		);

		Ticket ticket2 = new Ticket(
			2L, 1L, 100L, 1001L,
			LocalDateTime.of(2025, 8, 2, 12, 0),
			null,
			TicketStatus.PURCHASED
		);

		List<TicketResult> results = List.of(
			new TicketResult(ticket1, 10),
			new TicketResult(ticket2, 11)
		);

		given(readTicketUseCase.findTicketsByUserId(1L)).willReturn(results);

		// when & then
		mockMvc.perform(get("/api/tickets/me")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ticketResponses").isArray())
			.andExpect(jsonPath("$.ticketResponses.length()").value(2))
			.andExpect(jsonPath("$.ticketResponses[0].id").value(1))
			.andExpect(jsonPath("$.ticketResponses[0].seatNumber").value(10))
			.andExpect(jsonPath("$.ticketResponses[0].status").value("PURCHASED"))
			.andExpect(jsonPath("$.ticketResponses[1].id").value(2))
			.andExpect(jsonPath("$.ticketResponses[1].seatNumber").value(11));
	}

	@Test
	@WithMockCustomUser
	void refundTicket_success() throws Exception {
		Long ticketId = 1L;
		mockMvc.perform(post("/api/tickets/{ticketId}/refund", ticketId))
			.andExpect(status().isNoContent());
	}

	@Test
	@WithMockCustomUser
	void refundTicket_alreadyRefunded() throws Exception {
		Long ticketId = 1L;
		Long userId = 1L;
		doThrow(new TicketAlreadyRefundedException())
			.when(refundTicketUseCase).refund(ticketId, userId);

		mockMvc.perform(post("/api/tickets/{ticketId}/refund", ticketId))
			.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockCustomUser
	void refundTicket_forbidden() throws Exception {
		Long ticketId = 1L;
		Long userId = 1L;
		doThrow(new TicketAccessDeniedException())
			.when(refundTicketUseCase).refund(ticketId, userId);

		mockMvc.perform(post("/api/tickets/{ticketId}/refund", ticketId))
			.andExpect(status().isForbidden());
	}

	@Test
	@WithMockCustomUser
	void refundTicket_ticketNotFound() throws Exception {
		Long ticketId = 1L;
		Long userId = 1L;
		doThrow(new TicketNotFoundException())
			.when(refundTicketUseCase).refund(ticketId, userId);

		mockMvc.perform(post("/api/tickets/{ticketId}/refund", ticketId))
			.andExpect(status().isNotFound());
	}

	@Test
	@WithMockCustomUser
	void refundTicket_eventStarted() throws Exception {
		Long ticketId = 1L;
		Long userId = 1L;
		doThrow(new TicketCannotRefundAfterEventStartException())
			.when(refundTicketUseCase).refund(ticketId, userId);

		mockMvc.perform(post("/api/tickets/{ticketId}/refund", ticketId))
			.andExpect(status().isBadRequest());
	}
}