package com.kimgyeong.ticketingsimulation.queue.application;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.kimgyeong.ticketingsimulation.queue.application.port.in.GrantQueueAccessUseCase;
import com.kimgyeong.ticketingsimulation.queue.application.port.out.QueueRepositoryPort;
import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class GrantQueueAccessUseCaseImpl implements GrantQueueAccessUseCase {
	private final QueueRepositoryPort queueRepositoryPort;
	private final Clock clock;

	@Override
	public void grantAccess(Long userId, Long eventId) {
		QueueEntry entry = new QueueEntry(userId, eventId, LocalDateTime.now(clock));
		queueRepositoryPort.grantAccess(entry);
	}
}
