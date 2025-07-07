package com.kimgyeong.ticketingsimulation.event.domain.model;

import java.time.LocalDateTime;

public record Event(Long id, String title, String description, String imageUrl, LocalDateTime ticketingStartAt,
					LocalDateTime eventStartAt, int maxAttendees) {
	public boolean isTicketingOpen(LocalDateTime now) {
		return now.isAfter(ticketingStartAt);
	}

	public boolean isEventClosed(LocalDateTime now) {
		return now.isAfter(eventStartAt);
	}
}
