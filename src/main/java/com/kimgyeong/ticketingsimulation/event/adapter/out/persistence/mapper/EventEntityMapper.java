package com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.mapper;

import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.entity.EventEntity;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

public class EventEntityMapper {
	public static EventEntity toEntity(Event event) {
		return new EventEntity(
			event.id(),
			event.title(),
			event.description(),
			event.imageUrl(),
			event.ticketingStartAt(),
			event.eventStartAt(),
			event.maxAttendees(),
			event.virtualRequestCount(),
			event.userId()
		);
	}

	public static Event toDomain(EventEntity entity) {
		return new Event(
			entity.getId(),
			entity.getTitle(),
			entity.getDescription(),
			entity.getImageUrl(),
			entity.getTicketingStartAt(),
			entity.getEventStartAt(),
			entity.getMaxAttendees(),
			entity.getVirtualRequestCount(),
			entity.getUserId()
		);
	}
}
