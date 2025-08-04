package com.kimgyeong.ticketingsimulation.ticket.application.port;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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

import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.event.domain.model.SeatStatus;
import com.kimgyeong.ticketingsimulation.global.exception.SeatAccessDeniedException;
import com.kimgyeong.ticketingsimulation.queue.application.port.out.QueueRepositoryPort;
import com.kimgyeong.ticketingsimulation.ticket.application.port.in.PurchaseTicketUseCase;
import com.kimgyeong.ticketingsimulation.ticket.application.port.out.TicketRepositoryPort;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.TicketStatus;

@ExtendWith(MockitoExtension.class)
class PurchaseTicketUseCaseImplTest {
	final LocalDateTime NOW = LocalDateTime.of(2025, 1, 1, 10, 0);
	final Clock fixedClock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

	@Mock
	private TicketRepositoryPort ticketRepositoryPort;

	@Mock
	private SeatRepositoryPort seatRepositoryPort;

	@Mock
	private QueueRepositoryPort queueRepositoryPort;

	private PurchaseTicketUseCase useCase;

	@BeforeEach
	void setUp() {
		useCase = new PurchaseTicketUseCaseImpl(ticketRepositoryPort, seatRepositoryPort, queueRepositoryPort,
			fixedClock);
	}

	@Test
	void save() {
		Long userId = 1L;
		Long eventId = 100L;
		Long seatId = 100L;

		Seat seat = new Seat(seatId, eventId, SeatStatus.AVAILABLE, 1, LocalDateTime.now(), userId);
		Ticket savedTicket = new Ticket(
			1L,
			userId,
			eventId,
			seatId,
			LocalDateTime.now(fixedClock),
			null,
			TicketStatus.PURCHASED
		);

		when(seatRepositoryPort.findById(anyLong())).thenReturn(Optional.of(seat));
		when(ticketRepositoryPort.save(any(Ticket.class))).thenReturn(savedTicket);

		Long result = useCase.purchase(userId, eventId, seatId);

		assertThat(result).isEqualTo(savedTicket.id());
		verify(queueRepositoryPort).removeFromAccessQueue(anyLong(), anyLong());
	}

	@Test
	void save_whenUserNotHoldSeat() {
		Long userId = 1L;
		Long eventId = 100L;
		Long seatId = 100L;

		Seat seat = new Seat(seatId, eventId, SeatStatus.AVAILABLE, 1, LocalDateTime.now(), 2L);

		when(seatRepositoryPort.findById(anyLong())).thenReturn(Optional.of(seat));

		assertThrows(SeatAccessDeniedException.class, () -> useCase.purchase(userId, eventId, seatId));

	}
}