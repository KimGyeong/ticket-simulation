package com.kimgyeong.ticketingsimulation.ticket.adapter.out.persistence.mapper;

import com.kimgyeong.ticketingsimulation.ticket.adapter.out.persistence.entity.TicketEntity;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;

public class TicketEntityMapper {
	public static TicketEntity toEntity(Ticket ticket) {
		return new TicketEntity(
			ticket.id(),
			ticket.userId(),
			ticket.eventId(),
			ticket.seatId(),
			ticket.purchasedAt(),
			ticket.refundedAt(),
			ticket.status()
		);
	}

	public static Ticket toDomain(TicketEntity entity) {
		return new Ticket(
			entity.getId(),
			entity.getUserId(),
			entity.getEventId(),
			entity.getSeatId(),
			entity.getPurchasedAt(),
			entity.getRefundedAt(),
			entity.getStatus()
		);
	}
}
