package com.kimgyeong.ticketingsimulation.event.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimgyeong.ticketingsimulation.event.application.port.in.UpdateEventCommand;
import com.kimgyeong.ticketingsimulation.event.application.port.in.UpdateEventUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.global.exception.EventAccessDeniedException;
import com.kimgyeong.ticketingsimulation.global.exception.EventModificationTimeExpiredException;
import com.kimgyeong.ticketingsimulation.global.exception.EventNotFoundException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class UpdateEventUseCaseImpl implements UpdateEventUseCase {
	private final EventRepositoryPort eventRepositoryPort;

	@Override
	@Transactional
	public Event updateEvent(Long userId, Long eventId, UpdateEventCommand updateEventCommand) {
		Event event = eventRepositoryPort.findById(eventId)
			.orElseThrow(EventNotFoundException::new);

		if (event.isNotOwner(userId)) {
			throw new EventAccessDeniedException();
		}

		if (event.isTicketingOpen(LocalDateTime.now())) {
			throw new EventModificationTimeExpiredException();
		}

		Event updatedEvent = event.update(
			updateEventCommand.title(),
			updateEventCommand.description(),
			updateEventCommand.imageUrl(),
			updateEventCommand.ticketingStartAt(),
			updateEventCommand.eventStartAt(),
			updateEventCommand.maxAttendees()
		);

		return eventRepositoryPort.save(updatedEvent);
	}
}
