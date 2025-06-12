package com.kimgyeong.ticketingsimulation.event.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.event.domain.model.SeatStatus;

@ExtendWith(MockitoExtension.class)
class ReadSeatUseCaseImplTest {

	@InjectMocks
	ReadSeatUseCaseImpl readSeatUseCase;

	@Mock
	SeatRepositoryPort seatRepositoryPort;

	@Test
	void getSeatsByEventId() {
		Long eventId = 1L;
		Seat seat = new Seat(1L, eventId, SeatStatus.AVAILABLE, 1, null);
		given(seatRepositoryPort.findAllByEventId(eventId)).willReturn(List.of(seat));

		List<Seat> result = readSeatUseCase.getSeatsByEventId(eventId);

		assertThat(result.get(0).id()).isEqualTo(eventId);
	}
}