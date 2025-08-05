package com.kimgyeong.ticketingsimulation.ticket.application.port;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.global.exception.SeatNotFoundException;
import com.kimgyeong.ticketingsimulation.ticket.application.model.TicketResult;
import com.kimgyeong.ticketingsimulation.ticket.application.port.in.ReadTicketUseCase;
import com.kimgyeong.ticketingsimulation.ticket.application.port.out.TicketRepositoryPort;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class ReadTicketUseCaseImpl implements ReadTicketUseCase {
	private final TicketRepositoryPort ticketRepositoryPort;
	private final SeatRepositoryPort seatRepositoryPort;

	@Override
	public List<TicketResult> findTicketsByUserId(Long userId) {
		List<Ticket> tickets = ticketRepositoryPort.findTicketsByUserId(userId);

		return tickets.stream()
			.map(ticket -> {
				Seat seat = seatRepositoryPort.findById(ticket.seatId())
					.orElseThrow(SeatNotFoundException::new);
				return new TicketResult(ticket, seat.number());
			}).toList();

	}
}
