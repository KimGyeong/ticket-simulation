package com.kimgyeong.ticketingsimulation.ticket.application.port.out;

import java.util.List;
import java.util.Optional;

import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;

public interface TicketRepositoryPort {
	Ticket save(Ticket ticket);

	List<Ticket> findTicketsByUserId(Long userId);

	Optional<Ticket> findById(Long ticketId);

}
