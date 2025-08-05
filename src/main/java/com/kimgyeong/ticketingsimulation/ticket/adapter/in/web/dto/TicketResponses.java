package com.kimgyeong.ticketingsimulation.ticket.adapter.in.web.dto;

import java.util.List;

import com.kimgyeong.ticketingsimulation.ticket.application.model.TicketResult;

public record TicketResponses(List<TicketResponse> ticketResponses) {
	public static TicketResponses from(List<TicketResult> ticketResults) {
		List<TicketResponse> ticketResponses = ticketResults.stream()
			.map(TicketResponse::from)
			.toList();

		return new TicketResponses(ticketResponses);
	}
}
