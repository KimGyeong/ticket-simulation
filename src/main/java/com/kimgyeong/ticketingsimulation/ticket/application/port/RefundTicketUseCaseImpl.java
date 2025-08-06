package com.kimgyeong.ticketingsimulation.ticket.application.port;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.global.exception.EventNotFoundException;
import com.kimgyeong.ticketingsimulation.global.exception.SeatNotFoundException;
import com.kimgyeong.ticketingsimulation.global.exception.TicketAccessDeniedException;
import com.kimgyeong.ticketingsimulation.global.exception.TicketAlreadyRefundedException;
import com.kimgyeong.ticketingsimulation.global.exception.TicketNotFoundException;
import com.kimgyeong.ticketingsimulation.ticket.application.port.in.RefundTicketUseCase;
import com.kimgyeong.ticketingsimulation.ticket.application.port.out.TicketRepositoryPort;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.TicketStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class RefundTicketUseCaseImpl implements RefundTicketUseCase {
	private final TicketRepositoryPort ticketRepositoryPort;
	private final EventRepositoryPort eventRepositoryPort;
	private final SeatRepositoryPort seatRepositoryPort;
	private final Clock clock;

	@Override
	@Transactional
	public void refund(Long ticketId, Long userId) {
		Ticket ticket = ticketRepositoryPort.findById(ticketId)
			.orElseThrow(TicketNotFoundException::new);

		if (!ticket.userId().equals(userId)) {
			throw new TicketAccessDeniedException();
		}

		if (ticket.status() == TicketStatus.REFUNDED) {
			throw new TicketAlreadyRefundedException();
		}

		Event event = eventRepositoryPort.findById(ticket.eventId())
			.orElseThrow(EventNotFoundException::new);
		LocalDateTime now = LocalDateTime.now(clock);

		if (event.eventStartAt().isBefore(now)) {
			throw new TicketCannotRefundAfterEventStartException();
		}

		Seat seat = seatRepositoryPort.findById(ticket.seatId())
			.orElseThrow(SeatNotFoundException::new);
		Seat releasedSeat = seat.release();
		seatRepositoryPort.save(releasedSeat);

		Ticket refundedTicket = ticket.refund(now);
		ticketRepositoryPort.save(refundedTicket);
	}
}
