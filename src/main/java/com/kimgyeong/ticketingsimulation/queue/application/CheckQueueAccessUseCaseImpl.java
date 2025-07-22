package com.kimgyeong.ticketingsimulation.queue.application;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.kimgyeong.ticketingsimulation.queue.application.port.in.CheckQueueAccessUseCase;
import com.kimgyeong.ticketingsimulation.queue.application.port.out.QueueRepositoryPort;
import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class CheckQueueAccessUseCaseImpl implements CheckQueueAccessUseCase {
	private final QueueRepositoryPort queueRepositoryPort;
	private final Clock clock;

	@Override
	public boolean hasAccess(Long userId, Long eventId) {
		QueueEntry entry = new QueueEntry(userId, eventId, LocalDateTime.now(clock));
		return queueRepositoryPort.hasAccess(entry);
	}
}
