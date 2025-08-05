package com.kimgyeong.ticketingsimulation.ticket.application.port.in;

import java.util.List;

import com.kimgyeong.ticketingsimulation.ticket.application.model.TicketResult;

public interface ReadTicketUseCase {
	List<TicketResult> findTicketsByUserId(Long userId);
}
