package com.kimgyeong.ticketingsimulation.queue.adapter.in.web.dto;

public record CheckQueueAccessResponse(boolean hasAccess) {
	public static CheckQueueAccessResponse from(boolean hasAccess) {
		return new CheckQueueAccessResponse(hasAccess);
	}
}
