package com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.mapper;

import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.entity.SeatEntity;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;

public class SeatEntityMapper {
	public static SeatEntity toEntity(Seat seat) {
		return new SeatEntity(
			seat.id(),
			seat.eventId(),
			seat.status(),
			seat.number(),
			seat.heldAt()
		);
	}

	public static Seat toDomain(SeatEntity entity) {
		return new Seat(
			entity.getId(),
			entity.getEventId(),
			entity.getStatus(),
			entity.getNumber(),
			entity.getHeldAt()
		);
	}
}
