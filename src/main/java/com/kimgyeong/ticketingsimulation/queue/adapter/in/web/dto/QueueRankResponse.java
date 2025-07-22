package com.kimgyeong.ticketingsimulation.queue.adapter.in.web.dto;

public record QueueRankResponse(Long rank) {
	public static QueueRankResponse from(Long rank) {
		return new QueueRankResponse(rank);
	}
}
