package com.kimgyeong.ticketingsimulation.queue.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.global.exception.EventAlreadyStartedException;
import com.kimgyeong.ticketingsimulation.global.exception.TicketingNotOpenedException;
import com.kimgyeong.ticketingsimulation.queue.application.port.in.EnterQueueUseCase;
import com.kimgyeong.ticketingsimulation.queue.application.port.out.QueueRepositoryPort;
import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

@ExtendWith(MockitoExtension.class)
class EnterQueueUseCaseImplTest {
	final LocalDateTime NOW = LocalDateTime.of(2025, 1, 1, 10, 0);
	final Clock fixedClock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

	EnterQueueUseCase enterQueueUseCase;

	@Mock
	private QueueRepositoryPort queueRepositoryPort;

	@Mock
	private EventRepositoryPort eventRepositoryPort;

	@BeforeEach
	void setUp() {
		enterQueueUseCase = new EnterQueueUseCaseImpl(queueRepositoryPort, eventRepositoryPort);
	}

	@Test
	void enter() {
		Event event = new Event(1L, "테스트 이벤트", "테스트 설명", "테스트 이미지", LocalDateTime.now(fixedClock).minusMinutes(10),
			LocalDateTime.now(fixedClock).plusMinutes(30), 100, 1L);

		given(eventRepositoryPort.findById(100L)).willReturn(Optional.of(event));

		given(queueRepositoryPort.getUserRank(any(QueueEntry.class))).willReturn(0L);

		Long rank = enterQueueUseCase.enter(1L, 100L, LocalDateTime.now(fixedClock));

		assertThat(rank).isEqualTo(0L);
		verify(queueRepositoryPort).enterQueue(any(QueueEntry.class));
	}

	@Test
	void enter_whenTicketingNotOpen() {
		Event event = new Event(1L, "테스트 이벤트", "테스트 설명", "테스트 이미지", LocalDateTime.now(fixedClock).plusMinutes(10),
			LocalDateTime.now(fixedClock).plusMinutes(30), 100, 1L);

		given(eventRepositoryPort.findById(100L)).willReturn(Optional.of(event));

		assertThatThrownBy(() -> enterQueueUseCase.enter(1L, 100L, LocalDateTime.now(fixedClock)))
			.isInstanceOf(TicketingNotOpenedException.class);
	}

	@Test
	void enter_whenEventStarted() {
		Event event = new Event(1L, "테스트 이벤트", "테스트 설명", "테스트 이미지", LocalDateTime.now(fixedClock).minusMinutes(40),
			LocalDateTime.now(fixedClock).minusMinutes(30), 100, 1L);

		given(eventRepositoryPort.findById(100L)).willReturn(Optional.of(event));

		assertThatThrownBy(() -> enterQueueUseCase.enter(1L, 100L, LocalDateTime.now(fixedClock)))
			.isInstanceOf(EventAlreadyStartedException.class);
	}
}