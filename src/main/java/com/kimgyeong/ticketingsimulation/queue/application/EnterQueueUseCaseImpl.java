package com.kimgyeong.ticketingsimulation.queue.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.global.exception.EventAlreadyStartedException;
import com.kimgyeong.ticketingsimulation.global.exception.EventNotFoundException;
import com.kimgyeong.ticketingsimulation.global.exception.TicketingNotOpenedException;
import com.kimgyeong.ticketingsimulation.queue.application.port.in.EnterQueueUseCase;
import com.kimgyeong.ticketingsimulation.queue.application.port.out.QueueRepositoryPort;
import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class EnterQueueUseCaseImpl implements EnterQueueUseCase {
	private final QueueRepositoryPort queueRepositoryPort;
	private final EventRepositoryPort eventRepositoryPort;

	@Override
	public Long enter(Long userId, Long eventId, LocalDateTime now) {
		Event event = eventRepositoryPort.findById(eventId)
			.orElseThrow(EventNotFoundException::new);

		if (now.isBefore(event.ticketingStartAt())) {
			throw new TicketingNotOpenedException();
		}

		if (!now.isBefore(event.eventStartAt())) {
			throw new EventAlreadyStartedException();
		}

		QueueEntry entry = new QueueEntry(userId, eventId, LocalDateTime.now());
		queueRepositoryPort.enterQueue(entry);

		return queueRepositoryPort.getUserRank(entry);
	}
}
