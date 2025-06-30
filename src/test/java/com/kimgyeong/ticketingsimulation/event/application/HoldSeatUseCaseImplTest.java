package com.kimgyeong.ticketingsimulation.event.application;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.event.domain.model.SeatStatus;
import com.kimgyeong.ticketingsimulation.global.exception.SeatAlreadyHeldException;
import com.kimgyeong.ticketingsimulation.global.exception.SeatNotFoundException;
import com.kimgyeong.ticketingsimulation.global.lock.RedisLockService;

@ExtendWith(MockitoExtension.class)
class HoldSeatUseCaseImplTest {

	private final Long seatId = 1L;
	private final Long userId = 100L;
	@Mock
	private SeatRepositoryPort seatRepository;
	@Mock
	private RedisLockService redisLockService;
	@InjectMocks
	private HoldSeatUseCaseImpl holdSeatUseCase;

	@Test
	void holdSeat_success() {
		Seat availableSeat = new Seat(seatId, 10L, SeatStatus.AVAILABLE, 1, null, null);
		Seat expectedHeldSeat = availableSeat.hold(userId);

		when(seatRepository.findById(seatId)).thenReturn(Optional.of(availableSeat));

		doAnswer(invocation -> {
			Callable<?> task = invocation.getArgument(3);
			task.call();
			return null;
		}).when(redisLockService).runWithLock(anyString(), anyInt(), anyInt(), any());

		holdSeatUseCase.holdSeat(seatId, userId);

		verify(seatRepository).save(any(Seat.class));
	}

	@Test
	void holdSeat_whenSeatNotExist() {
		when(seatRepository.findById(seatId)).thenReturn(Optional.empty());

		doAnswer(invocation -> {
			Callable<?> task = invocation.getArgument(3);
			task.call();
			return null;
		}).when(redisLockService).runWithLock(anyString(), anyInt(), anyInt(), any());

		assertThrows(SeatNotFoundException.class, () -> holdSeatUseCase.holdSeat(seatId, userId));
	}

	@Test
	void holdSeat_whenSeatAlreadyHeld() {
		Seat heldSeat = new Seat(seatId, 10L, SeatStatus.TEMPORARY_HOLD, 1, LocalDateTime.now(), 999L);

		when(seatRepository.findById(seatId)).thenReturn(Optional.of(heldSeat));

		doAnswer(invocation -> {
			Callable<?> task = invocation.getArgument(3);
			task.call();
			return null;
		}).when(redisLockService).runWithLock(anyString(), anyInt(), anyInt(), any());

		assertThrows(SeatAlreadyHeldException.class, () -> holdSeatUseCase.holdSeat(seatId, userId));
	}
}