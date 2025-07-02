package com.kimgyeong.ticketingsimulation.event.adapter.in;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kimgyeong.ticketingsimulation.event.application.port.in.ReleaseSeatUseCase;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class ReleaseSeatScheduler {
	public static final int RELEASE_SEAT_THRESHOLD_MINUTE = 2;
	private final Clock clock;
	private final ReleaseSeatUseCase releaseSeatUseCase;

	@Scheduled(fixedRate = 1000)
	public void releaseSeats() {
		LocalDateTime now = LocalDateTime.now(clock);
		LocalDateTime threshold = now.minusMinutes(RELEASE_SEAT_THRESHOLD_MINUTE);
		releaseSeatUseCase.releaseExpiredSeats(threshold);
	}
}
