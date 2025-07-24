package com.kimgyeong.ticketingsimulation.queue.messaging.message;

import java.time.LocalDateTime;

public record EnterQueueMessage(Long userId, Long eventId, LocalDateTime enteredAt) {
}
