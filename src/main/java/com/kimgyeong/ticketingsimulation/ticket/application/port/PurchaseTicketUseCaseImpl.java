package com.kimgyeong.ticketingsimulation.ticket.application.port;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.global.exception.SeatAccessDeniedException;
import com.kimgyeong.ticketingsimulation.global.exception.SeatNotFoundException;
import com.kimgyeong.ticketingsimulation.ticket.application.port.in.PurchaseTicketUseCase;
import com.kimgyeong.ticketingsimulation.ticket.application.port.out.TicketRepositoryPort;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.TicketStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class PurchaseTicketUseCaseImpl implements PurchaseTicketUseCase {
	private final TicketRepositoryPort ticketRepositoryPort;
	private final SeatRepositoryPort seatRepositoryPort;
	private final Clock clock;

	@Override
	public Long purchase(Long userId, Long eventId, Long seatId) {
		Seat seat = seatRepositoryPort.findById(seatId)
			.orElseThrow(SeatNotFoundException::new);

		if (seat.isHoldExpired() || !seat.heldUserId().equals(userId)) {
			throw new SeatAccessDeniedException();
		}

		Ticket ticket = new Ticket(
			null,
			userId,
			eventId,
			seatId,
			LocalDateTime.now(clock),
			null,
			TicketStatus.PURCHASED
		);

		return ticketRepositoryPort.save(ticket)
			.id();
	}
}
