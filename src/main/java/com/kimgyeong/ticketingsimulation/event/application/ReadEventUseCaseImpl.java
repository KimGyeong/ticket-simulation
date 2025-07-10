package com.kimgyeong.ticketingsimulation.event.application;

import org.springframework.stereotype.Service;

import com.kimgyeong.ticketingsimulation.event.application.model.EventDetailResult;
import com.kimgyeong.ticketingsimulation.event.application.port.in.ReadEventUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.global.exception.EventNotFoundException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class ReadEventUseCaseImpl implements ReadEventUseCase {
	private final EventRepositoryPort eventRepositoryPort;
	private final SeatRepositoryPort seatRepositoryPort;

	@Override
	public EventDetailResult findById(Long id) {
		Event event = eventRepositoryPort.findById(id)
			.orElseThrow(EventNotFoundException::new);

		Long availableSeatCount = seatRepositoryPort.countAvailableByEventId(event.id());

		return new EventDetailResult(event, availableSeatCount);
	}
}
