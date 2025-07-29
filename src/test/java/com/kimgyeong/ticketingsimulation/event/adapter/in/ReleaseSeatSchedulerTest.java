package com.kimgyeong.ticketingsimulation.event.adapter.in;

import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.event.application.port.in.ReleaseSeatUseCase;

@ExtendWith(MockitoExtension.class)
class ReleaseSeatSchedulerTest {

	@Mock
	private ReleaseSeatUseCase releaseSeatUseCase;

	private Clock fixedClock;
	private ReleaseSeatScheduler scheduler;

	@BeforeEach
	void setUp() {
		LocalDateTime fixedTime = LocalDateTime.of(2025, 7, 2, 12, 0, 0);
		fixedClock = Clock.fixed(fixedTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
		scheduler = new ReleaseSeatScheduler(fixedClock, releaseSeatUseCase);
	}

	@Test
	void releaseSeats_shouldCallUseCaseWithThresholdTime() {
		// given
		LocalDateTime expectedThreshold = LocalDateTime.of(2025, 7, 2, 11, 58, 0); // 2분 전

		// when
		scheduler.releaseSeats();

		// then
		verify(releaseSeatUseCase).releaseExpiredSeats(expectedThreshold);
	}
}