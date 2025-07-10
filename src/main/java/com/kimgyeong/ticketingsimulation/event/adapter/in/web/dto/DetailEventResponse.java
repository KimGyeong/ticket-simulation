package com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto;

import com.kimgyeong.ticketingsimulation.event.application.model.EventDetailResult;

public record DetailEventResponse(
	Long id,
	String title,
	String description,
	String imageUrl,
	String ticketingStartAt,
	String eventStartAt,
	int maxAttendees,
	long availableSeatCount
) {
	public static DetailEventResponse from(EventDetailResult result) {
		return new DetailEventResponse(
			result.event().id(),
			result.event().title(),
			result.event().description(),
			result.event().imageUrl(),
			result.event().ticketingStartAt().toString(),
			result.event().eventStartAt().toString(),
			result.event().maxAttendees(),
			result.availableSeatCount()
		);
	}
}
