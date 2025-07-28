package com.kimgyeong.ticketingsimulation.queue.application;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.kimgyeong.ticketingsimulation.queue.application.port.in.GrantQueueAccessUseCase;
import com.kimgyeong.ticketingsimulation.queue.application.port.out.QueueRepositoryPort;
import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class GrantQueueAccessUseCaseImpl implements GrantQueueAccessUseCase {
	private static final int MAX_ALLOWED_USER_COUNT = 1000;

	private final QueueRepositoryPort queueRepositoryPort;
	private final Clock clock;

	@Override
	public void grantAccess(Long userId, Long eventId) {
		QueueEntry entry = new QueueEntry(userId, eventId, LocalDateTime.now(clock));
		if (queueRepositoryPort.hasAccess(entry)) {
			log.info("입장 허용 생략(이미 입장한 사용자): userId={}, eventId={}", userId, eventId);
			return;
		}

		int currentGrantedCount = queueRepositoryPort.countGrantedUsers(eventId);
		if (currentGrantedCount >= MAX_ALLOWED_USER_COUNT) {
			log.info("입장 거부: 인원 초과 - userId={}, eventId={}, 현재 인원={}", userId, eventId, currentGrantedCount);
			return;
		}

		queueRepositoryPort.grantAccess(entry);
		log.info("입장 허용: userId={}, eventId={}, 현재 인원={}", userId, eventId, currentGrantedCount);
	}
}
