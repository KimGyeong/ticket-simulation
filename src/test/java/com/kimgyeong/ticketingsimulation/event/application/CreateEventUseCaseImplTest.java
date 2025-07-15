package com.kimgyeong.ticketingsimulation.event.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventCommand;
import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.event.domain.model.SeatStatus;

@ExtendWith(MockitoExtension.class)
class CreateEventUseCaseImplTest {

	@Mock
	private SeatRepositoryPort seatPort;

	@Mock
	private EventRepositoryPort eventPort;

	@InjectMocks
	private CreateEventUseCaseImpl createEventUseCase;

	@Test
	void createEvent() {
		CreateEventCommand command = new CreateEventCommand(
			"테스트 이벤트",
			"테스트 설명",
			"테스트 이미지",
			LocalDateTime.of(2025, 8, 1, 18, 0),
			LocalDateTime.of(2025, 8, 2, 20, 0),
			3
		);

		Event savedEvent = new Event(
			1L,
			command.title(),
			command.description(),
			command.imageUrl(),
			command.ticketingStartAt(),
			command.eventStartAt(),
			command.maxAttendees(),
			1L
		);

		given(eventPort.save(any())).willReturn(savedEvent);

		Long resultId = createEventUseCase.createEvent(1L, command);

		assertThat(resultId).isEqualTo(1L);

		ArgumentCaptor<List<Seat>> seatCaptor = ArgumentCaptor.forClass(List.class);
		verify(seatPort).saveAll(seatCaptor.capture());

		List<Seat> savedSeats = seatCaptor.getValue();
		assertThat(savedSeats).hasSize(3);
		assertThat(savedSeats)
			.extracting(Seat::eventId)
			.allMatch(id -> id.equals(1L));
		assertThat(savedSeats)
			.extracting(Seat::status)
			.allMatch(status -> status == SeatStatus.AVAILABLE);
		assertThat(savedSeats)
			.extracting(Seat::number)
			.containsExactly(1, 2, 3);
	}
}