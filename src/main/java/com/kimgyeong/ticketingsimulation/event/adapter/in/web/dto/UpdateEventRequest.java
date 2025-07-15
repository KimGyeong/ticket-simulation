package com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimgyeong.ticketingsimulation.event.application.port.in.UpdateEventCommand;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateEventRequest(
	@NotBlank(message = "제목은 필수입니다.")
	String title,

	@NotBlank(message = "설명은 필수입니다.")
	String description,

	@NotBlank(message = "설명은 필수입니다.")
	String imageUrl,

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime ticketingStartAt,

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime eventStartAt,

	@Min(value = 1, message = "최대 수용 인원은 1명 이상이어야 합니다.")
	int maxAttendees
) {

	public UpdateEventCommand toCommand() {
		return new UpdateEventCommand(
			title,
			description,
			imageUrl,
			ticketingStartAt,
			eventStartAt,
			maxAttendees
		);
	}
}
