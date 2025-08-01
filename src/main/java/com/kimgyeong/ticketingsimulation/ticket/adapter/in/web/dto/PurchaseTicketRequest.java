package com.kimgyeong.ticketingsimulation.ticket.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;

public record PurchaseTicketRequest(
	@NotNull(message = "이벤트 ID는 필수입니다.")
	Long eventId,

	@NotNull(message = "좌석 ID는 필수입니다.")
	Long seatId
) {
}
