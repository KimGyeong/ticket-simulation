package com.kimgyeong.ticketingsimulation.queue.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.queue.application.port.in.ReadQueueRankUseCase;
import com.kimgyeong.ticketingsimulation.queue.application.port.out.QueueRepositoryPort;
import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

@ExtendWith(MockitoExtension.class)
class ReadQueueRankUseCaseImplTest {
	final Clock fixedClock = Clock.fixed(
		LocalDateTime.of(2025, 1, 1, 10, 0).toInstant(ZoneOffset.UTC),
		ZoneOffset.UTC
	);
	@Mock
	private QueueRepositoryPort port;
	private ReadQueueRankUseCase readQueueRankUseCase;

	@BeforeEach
	void setUp() {
		readQueueRankUseCase = new ReadQueueRankUseCaseImpl(port, fixedClock);
	}

	@Test
	void getRank() {
		Long userId = 1L;
		Long eventId = 100L;
		QueueEntry entry = new QueueEntry(userId, eventId, LocalDateTime.now(fixedClock));
		given(port.getUserRank(any(QueueEntry.class))).willReturn(5L);

		Long result = readQueueRankUseCase.getRank(userId, eventId);

		assertThat(result).isEqualTo(5L);
		verify(port).getUserRank(entry);
	}

	@Test
	void getRank_whenUserNotInQueue_returnsNull() {
		Long userId = 1L;
		Long eventId = 100L;
		given(port.getUserRank(any(QueueEntry.class))).willReturn(null);

		Long result = readQueueRankUseCase.getRank(userId, eventId);

		assertThat(result).isNull();
		verify(port).getUserRank(any(QueueEntry.class));
	}
}