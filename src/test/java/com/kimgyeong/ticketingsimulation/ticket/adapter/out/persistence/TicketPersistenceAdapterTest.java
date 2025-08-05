package com.kimgyeong.ticketingsimulation.ticket.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.ticket.adapter.out.persistence.entity.TicketEntity;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.Ticket;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.TicketStatus;

@ExtendWith(MockitoExtension.class)
class TicketPersistenceAdapterTest {
	@Mock
	private JpaTicketRepository repository;

	@InjectMocks
	private TicketPersistenceAdapter adapter;

	@Test
	void save() {
		Ticket ticket = new Ticket(null, 1L, 10L, 5L, LocalDateTime.now(), null, TicketStatus.PURCHASED);
		TicketEntity savedEntity = new TicketEntity(100L, 1L, 10L, 5L, ticket.purchasedAt(), null,
			TicketStatus.PURCHASED);

		when(repository.save(any(TicketEntity.class))).thenReturn(savedEntity);

		Ticket result = adapter.save(ticket);

		assertThat(result.userId()).isEqualTo(savedEntity.getUserId());

		ArgumentCaptor<TicketEntity> captor = ArgumentCaptor.forClass(TicketEntity.class);
		verify(repository).save(captor.capture());
	}

	@Test
	void findTicketsByUserId() {
		Ticket ticket = new Ticket(100L, 1L, 10L, 5L, LocalDateTime.now(), null, TicketStatus.PURCHASED);
		TicketEntity savedEntity = new TicketEntity(100L, 1L, 10L, 5L, ticket.purchasedAt(), null,
			TicketStatus.PURCHASED);

		when(repository.findAllByUserIdOrderByCreatedAtDesc(anyLong())).thenReturn(List.of(savedEntity));

		List<Ticket> result = adapter.findTicketsByUserId(1L);

		assertThat(result.get(0).userId()).isEqualTo(savedEntity.getUserId());
		assertThat(result.get(0).seatId()).isEqualTo(savedEntity.getSeatId());
	}
}