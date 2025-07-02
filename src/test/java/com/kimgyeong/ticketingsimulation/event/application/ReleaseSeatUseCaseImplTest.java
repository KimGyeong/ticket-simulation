package com.kimgyeong.ticketingsimulation.event.application;

import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
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
class ReleaseSeatUseCaseImplTest {
	@Mock
	private SeatRepositoryPort seatRepositoryPort;

	@InjectMocks
	private ReleaseSeatUseCaseImpl releaseSeatUseCase;

	@Test
	void releaseExpiredSeats() {
		LocalDateTime now = LocalDateTime.now();
		Seat expired1 = new Seat(1L, 1L, SeatStatus.TEMPORARY_HOLD, 1, now.minusMinutes(3), 1L);
		Seat expired2 = new Seat(2L, 1L, SeatStatus.TEMPORARY_HOLD, 2, now.minusMinutes(5), 2L);

		given(seatRepositoryPort.findAllExpiredHeldSeats(any()))
			.willReturn(List.of(expired1, expired2));

		releaseSeatUseCase.releaseExpiredSeats(now.minusMinutes(2));

		then(seatRepositoryPort).should().saveAll(
			argThat(seats -> seats.stream().allMatch(seat -> seat.status() == SeatStatus.AVAILABLE))
		);
	}
}