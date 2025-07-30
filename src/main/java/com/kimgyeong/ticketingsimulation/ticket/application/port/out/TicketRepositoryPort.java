package com.kimgyeong.ticketingsimulation.ticket.application.port.out;

import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;

public interface TicketRepositoryPort {
	Ticket save(Ticket ticket);
}
