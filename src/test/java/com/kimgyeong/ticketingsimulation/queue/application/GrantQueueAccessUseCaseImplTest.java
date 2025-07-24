package com.kimgyeong.ticketingsimulation.queue.application;

import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.queue.application.port.in.GrantQueueAccessUseCase;
import com.kimgyeong.ticketingsimulation.queue.application.port.out.QueueRepositoryPort;
import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

@ExtendWith(MockitoExtension.class)
class GrantQueueAccessUseCaseImplTest {
	private final Clock fixedClock = Clock.fixed(LocalDateTime.of(2025, 1, 1, 10, 0).toInstant(ZoneOffset.UTC),
		ZoneOffset.UTC);
	@Mock
	private QueueRepositoryPort port;
	private GrantQueueAccessUseCase useCase;

	@BeforeEach
	void setUp() {
		useCase = new GrantQueueAccessUseCaseImpl(port, fixedClock);
	}

	@Test
	void grantAccess() {
		Long userId = 1L;
		Long eventId = 100L;

		useCase.grantAccess(userId, eventId);

		verify(port).grantAccess(new QueueEntry(userId, eventId, LocalDateTime.now(fixedClock)));
	}
}