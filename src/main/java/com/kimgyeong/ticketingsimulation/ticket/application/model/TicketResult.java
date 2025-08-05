package com.kimgyeong.ticketingsimulation.ticket.application.model;

import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;

public record TicketResult(Ticket ticket, int seatNumber) {
}
