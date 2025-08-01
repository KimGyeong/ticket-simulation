package com.kimgyeong.ticketingsimulation.ticket.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.kimgyeong.ticketingsimulation.global.controller.AbstractControllerTest;
import com.kimgyeong.ticketingsimulation.global.security.annotation.WithMockCustomUser;
import com.kimgyeong.ticketingsimulation.ticket.adapter.in.web.dto.PurchaseTicketRequest;
import com.kimgyeong.ticketingsimulation.ticket.application.port.in.PurchaseTicketUseCase;

@WebMvcTest(TicketController.class)
class TicketControllerTest extends AbstractControllerTest {
	@MockitoBean
	private PurchaseTicketUseCase purchaseTicketUseCase;

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
}