package com.kimgyeong.ticketingsimulation.queue.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.queue.application.port.in.CheckQueueAccessUseCase;
import com.kimgyeong.ticketingsimulation.queue.application.port.out.QueueRepositoryPort;
import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

@ExtendWith(MockitoExtension.class)
class CheckQueueAccessUseCaseImplTest {
	private final Clock fixedClock = Clock.fixed(
		LocalDateTime.of(2025, 1, 1, 12, 0).toInstant(ZoneOffset.UTC),
		ZoneOffset.UTC
	);
	@Mock
	private QueueRepositoryPort port;
	private CheckQueueAccessUseCase useCase;

	@BeforeEach
	void setUp() {
		useCase = new CheckQueueAccessUseCaseImpl(port, fixedClock);
	}

	@Test
	void hasAccess() {
		QueueEntry entry = new QueueEntry(1L, 100L, LocalDateTime.now(fixedClock));
		given(port.hasAccess(entry)).willReturn(true);

		boolean result = useCase.hasAccess(1L, 100L);

		assertThat(result).isTrue();
	}

	@Test
	void hasAccess_returnFalse() {
		QueueEntry entry = new QueueEntry(2L, 100L, LocalDateTime.now(fixedClock));
		given(port.hasAccess(entry)).willReturn(false);

		boolean result = useCase.hasAccess(2L, 100L);

		assertThat(result).isFalse();
	}
}