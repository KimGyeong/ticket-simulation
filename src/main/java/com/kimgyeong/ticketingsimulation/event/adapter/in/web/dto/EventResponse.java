package com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto;

import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

public record EventResponse(
	Long id,
	String title,
	String description,
	String imageUrl,
	String ticketingStartAt,
	String eventStartAt,
	int maxAttendees
) {
	public static EventResponse from(Event event) {
		return new EventResponse(
			event.id(),
			event.title(),
			event.description(),
			event.imageUrl(),
			event.ticketingStartAt().toString(),
			event.eventStartAt().toString(),
			event.maxAttendees()
		);
	}
}
