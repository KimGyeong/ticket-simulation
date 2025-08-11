package com.kimgyeong.ticketingsimulation.event.application.port.in;

import java.time.LocalDateTime;

public record CreateEventCommand(
	String title,
	String description,
	String imageUrl,
	LocalDateTime ticketingStartAt,
	LocalDateTime eventStartAt,
	long virtualRequestCount,
	int maxAttendees
) {
}
