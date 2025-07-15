package com.kimgyeong.ticketingsimulation.event.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventCommand;
import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.factory.SeatFactory;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class CreateEventUseCaseImpl implements CreateEventUseCase {
	private final EventRepositoryPort eventRepositoryPort;
	private final SeatRepositoryPort seatRepositoryPort;

	@Override
	@Transactional
	public Long createEvent(Long userId, CreateEventCommand command) {
		Event event = new Event(
			null,
			command.title(),
			command.description(),
			command.imageUrl(),
			command.ticketingStartAt(),
			command.eventStartAt(),
			command.maxAttendees(),
			userId
		);
		Event savedEvent = eventRepositoryPort.save(event);

		List<Seat> seats = SeatFactory.createSeats(savedEvent.id(), command.maxAttendees());

		seatRepositoryPort.saveAll(seats);

		return savedEvent.id();
	}
}
