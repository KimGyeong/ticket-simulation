package com.kimgyeong.ticketingsimulation.ticket.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.kimgyeong.ticketingsimulation.ticket.adapter.out.persistence.entity.TicketEntity;
import com.kimgyeong.ticketingsimulation.ticket.adapter.out.persistence.mapper.TicketEntityMapper;
import com.kimgyeong.ticketingsimulation.ticket.application.port.out.TicketRepositoryPort;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class TicketPersistenceAdapter implements TicketRepositoryPort {
	private final JpaTicketRepository ticketRepository;

	@Override
	public Ticket save(Ticket ticket) {
		TicketEntity entity = TicketEntityMapper.toEntity(ticket);
		TicketEntity savedTicketEntity = ticketRepository.save(entity);
		return TicketEntityMapper.toDomain(savedTicketEntity);
	}
}
