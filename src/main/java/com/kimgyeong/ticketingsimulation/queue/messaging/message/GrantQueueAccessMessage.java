package com.kimgyeong.ticketingsimulation.queue.messaging.message;

public record GrantQueueAccessMessage(Long userId, Long eventId) {
}
