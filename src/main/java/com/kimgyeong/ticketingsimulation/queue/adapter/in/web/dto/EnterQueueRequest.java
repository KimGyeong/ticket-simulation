package com.kimgyeong.ticketingsimulation.queue.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;

public record EnterQueueRequest(
	@NotNull(message = "이벤트 ID는 필수입니다.")
	Long eventId
) {
}
