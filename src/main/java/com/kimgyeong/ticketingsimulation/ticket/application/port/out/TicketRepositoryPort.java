package com.kimgyeong.ticketingsimulation.ticket.application.port.out;

import java.util.List;

import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;

public interface TicketRepositoryPort {
	Ticket save(Ticket ticket);

	List<Ticket> findTicketsByUserId(Long userId);
}
