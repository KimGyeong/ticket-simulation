package com.kimgyeong.ticketingsimulation.event.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimgyeong.ticketingsimulation.event.application.port.in.DeleteEventUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.global.exception.EventAccessDeniedException;
import com.kimgyeong.ticketingsimulation.global.exception.EventNotFoundException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class DeleteEventUseCaseImpl implements DeleteEventUseCase {
	private final EventRepositoryPort eventRepositoryPort;

	@Override
	@Transactional
	public void deleteById(Long eventId, Long userId) {
		Event event = eventRepositoryPort.findById(eventId)
			.orElseThrow(EventNotFoundException::new);

		if (event.isNotOwner(userId)) {
			throw new EventAccessDeniedException();
		}

		eventRepositoryPort.deleteById(eventId);
	}
}
