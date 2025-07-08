package com.kimgyeong.ticketingsimulation.event.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimgyeong.ticketingsimulation.event.application.port.in.HoldSeatUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.global.exception.SeatNotFoundException;
import com.kimgyeong.ticketingsimulation.global.lock.RedisLockService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class HoldSeatUseCaseImpl implements HoldSeatUseCase {
	private final SeatRepositoryPort seatRepository;
	private final RedisLockService redisLockService;

	@Override
	@Transactional
	public void holdSeat(Long seatId, Long userId) {
		String lockKey = "seat:" + seatId;

		redisLockService.runWithLock(lockKey, 1, 3, () -> {
			Seat seat = seatRepository.findById(seatId)
				.orElseThrow(SeatNotFoundException::new);

			Seat heldSeat = seat.hold(userId);
			seatRepository.save(heldSeat);
			return null;
		});
	}
}
