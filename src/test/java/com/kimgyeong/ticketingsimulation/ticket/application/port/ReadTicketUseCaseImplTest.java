package com.kimgyeong.ticketingsimulation.ticket.application.port;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.event.domain.model.SeatStatus;
import com.kimgyeong.ticketingsimulation.ticket.application.model.TicketResult;
import com.kimgyeong.ticketingsimulation.ticket.application.port.out.TicketRepositoryPort;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.TicketStatus;

@ExtendWith(MockitoExtension.class)
class ReadTicketUseCaseImplTest {
	@Mock
	private TicketRepositoryPort ticketRepositoryPort;

	@Mock
	private SeatRepositoryPort seatRepositoryPort;

	@InjectMocks
	private ReadTicketUseCaseImpl readTicketUseCase;

	@Test
	void findTicketsByUserId() {
		Long userId = 1L;
		Ticket ticket = new Ticket(100L, userId, 10L, 5L, LocalDateTime.now(), null, TicketStatus.PURCHASED);
		Seat heldSeat = new Seat(5L, 10L, SeatStatus.BOOKED, 1, LocalDateTime.now(), userId);
		given(ticketRepositoryPort.findTicketsByUserId(userId)).willReturn(List.of(ticket));
		given(seatRepositoryPort.findById(5L)).willReturn(Optional.of(heldSeat));

		List<TicketResult> result = readTicketUseCase.findTicketsByUserId(userId);

		assertThat(result.get(0).seatNumber()).isEqualTo(heldSeat.number());
		assertThat(result.get(0).ticket().eventId()).isEqualTo(ticket.eventId());
	}
}