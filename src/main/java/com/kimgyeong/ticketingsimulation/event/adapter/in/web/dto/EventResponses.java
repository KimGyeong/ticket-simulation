package com.kimgyeong.ticketingsimulation.event.adapter.in.web.dto;

import java.util.List;

import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

public record EventResponses(List<EventResponse> eventResponses) {

	public static EventResponses from(List<Event> allEvents) {
		List<EventResponse> eventResponses = allEvents.stream()
			.map(EventResponse::from)
			.toList();
		return new EventResponses(eventResponses);
	}
}
