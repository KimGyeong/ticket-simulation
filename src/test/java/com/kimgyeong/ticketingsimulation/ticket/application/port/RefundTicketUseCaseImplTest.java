package com.kimgyeong.ticketingsimulation.ticket.application.port;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.event.domain.model.SeatStatus;
import com.kimgyeong.ticketingsimulation.global.exception.EventNotFoundException;
import com.kimgyeong.ticketingsimulation.global.exception.TicketAccessDeniedException;
import com.kimgyeong.ticketingsimulation.global.exception.TicketAlreadyRefundedException;
import com.kimgyeong.ticketingsimulation.global.exception.TicketNotFoundException;
import com.kimgyeong.ticketingsimulation.ticket.application.port.out.TicketRepositoryPort;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.TicketStatus;

@ExtendWith(MockitoExtension.class)
class RefundTicketUseCaseImplTest {
	private final LocalDateTime now = LocalDateTime.of(2025, 1, 1, 12, 0);
	private final Clock fixedClock = Clock.fixed(now.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
	@Mock
	private TicketRepositoryPort ticketRepositoryPort;
	@Mock
	private EventRepositoryPort eventRepositoryPort;
	@Mock
	private SeatRepositoryPort seatRepositoryPort;
	private RefundTicketUseCaseImpl refundTicketUseCase;

	@BeforeEach
	void setUp() {
		refundTicketUseCase = new RefundTicketUseCaseImpl(ticketRepositoryPort, eventRepositoryPort, seatRepositoryPort,
			fixedClock);
	}

	@Test
	void refund() {
		Long ticketId = 1L;
		Long userId = 100L;
		Long eventId = 10L;
		Long seatId = 500L;

		Ticket originalTicket = new Ticket(ticketId, userId, eventId, seatId, now.minusDays(1), null,
			TicketStatus.PURCHASED);
		Event event = new Event(eventId, "테스트 이벤트", "테스트 설명", "테스트 이미지", now.minusHours(1), now.plusHours(1), 100,
			100L);
		Seat seat = new Seat(seatId, eventId, SeatStatus.BOOKED, 1, now.minusDays(1), userId);

		when(ticketRepositoryPort.findById(ticketId)).thenReturn(Optional.of(originalTicket));
		when(eventRepositoryPort.findById(eventId)).thenReturn(Optional.of(event));
		when(seatRepositoryPort.findById(seatId)).thenReturn(Optional.of(seat));

		refundTicketUseCase.refund(ticketId, userId);

		verify(ticketRepositoryPort).save(argThat(saved ->
			saved.status() == TicketStatus.REFUNDED &&
				saved.refundedAt().equals(now)
		));

		verify(seatRepositoryPort).save(argThat(released ->
			released.status() == SeatStatus.AVAILABLE &&
				released.heldUserId() == null &&
				released.heldAt() == null
		));
	}

	@Test
	void refund_whenTicketNotFound() {
		when(ticketRepositoryPort.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> refundTicketUseCase.refund(1L, 100L))
			.isInstanceOf(TicketNotFoundException.class);
	}

	@Test
	void refund_whenTicketOwnerIsNotUser() {
		Ticket ticket = new Ticket(1L, 999L, 10L, 500L, now.minusDays(1), null, TicketStatus.PURCHASED);
		when(ticketRepositoryPort.findById(1L)).thenReturn(Optional.of(ticket));

		assertThatThrownBy(() -> refundTicketUseCase.refund(1L, 100L))
			.isInstanceOf(TicketAccessDeniedException.class);
	}

	@Test
	void refund_whenTicketAlreadyRefunded() {
		Ticket ticket = new Ticket(1L, 100L, 10L, 500L, now.minusDays(1), now.minusHours(1), TicketStatus.REFUNDED);
		when(ticketRepositoryPort.findById(1L)).thenReturn(Optional.of(ticket));

		assertThatThrownBy(() -> refundTicketUseCase.refund(1L, 100L))
			.isInstanceOf(TicketAlreadyRefundedException.class);
	}

	@Test
	void refund_whenEventIsNotExist() {
		Ticket ticket = new Ticket(1L, 100L, 10L, 500L, now.minusDays(1), null, TicketStatus.PURCHASED);
		when(ticketRepositoryPort.findById(1L)).thenReturn(Optional.of(ticket));
		when(eventRepositoryPort.findById(10L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> refundTicketUseCase.refund(1L, 100L))
			.isInstanceOf(EventNotFoundException.class);
	}

	@Test
	void refund_whenEventAlreadyStarted() {
		Ticket ticket = new Ticket(1L, 100L, 10L, 500L, now.minusDays(1), null, TicketStatus.PURCHASED);
		Event event = new Event(10L, "테스트 이벤트", "테스트 설명", "테스트 이미지", now.minusDays(1), now.minusHours(1), 100, 100L);

		when(ticketRepositoryPort.findById(1L)).thenReturn(Optional.of(ticket));
		when(eventRepositoryPort.findById(10L)).thenReturn(Optional.of(event));

		assertThatThrownBy(() -> refundTicketUseCase.refund(1L, 100L))
			.isInstanceOf(TicketCannotRefundAfterEventStartException.class);
	}
}