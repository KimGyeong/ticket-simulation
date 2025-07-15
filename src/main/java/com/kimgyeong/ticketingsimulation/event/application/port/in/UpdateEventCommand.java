package com.kimgyeong.ticketingsimulation.event.application.port.in;

import java.time.LocalDateTime;

public record UpdateEventCommand(
	String title,
	String description,
	String imageUrl,
	LocalDateTime ticketingStartAt,
	LocalDateTime eventStartAt,
	int maxAttendees
) {
}
