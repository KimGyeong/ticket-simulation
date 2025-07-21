package com.kimgyeong.ticketingsimulation.queue.adapter.in.web.dto;

public record EnterQueueResponse(Long rank) {
	public static EnterQueueResponse from(Long rank) {
		return new EnterQueueResponse(rank);
	}
}
