package com.kimgyeong.ticketingsimulation.event.domain.model;

import java.time.LocalDateTime;

public record Event(
	Long id,
	String title,
	String description,
	String imageUrl,
	LocalDateTime ticketingStartAt,
	LocalDateTime eventStartAt,
	int maxAttendees,
	Long virtualRequestCount,
	Long userId
) {
	public boolean isTicketingOpen(LocalDateTime now) {
		return now.isAfter(ticketingStartAt);
	}

	public boolean isEventClosed(LocalDateTime now) {
		return now.isAfter(eventStartAt);
	}

	public boolean isNotOwner(Long userId) {
		return !this.userId.equals(userId);
	}

	public Event update(
		String title,
		String description,
		String imageUrl,
		LocalDateTime ticketingStartAt,
		LocalDateTime eventStartAt,
		Long virtualRequestCount,
		int maxAttendees
	) {
		return new Event(
			this.id,
			title,
			description,
			imageUrl,
			ticketingStartAt,
			eventStartAt,
			maxAttendees,
			virtualRequestCount,
			this.userId
		);
	}
}
