package com.kimgyeong.ticketingsimulation.queue.domain;

import java.time.LocalDateTime;

public record QueueEntry(
	Long userId,
	Long eventId,
	LocalDateTime enteredAt
) {
}
