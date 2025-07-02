package com.kimgyeong.ticketingsimulation.event.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kimgyeong.ticketingsimulation.event.application.port.in.ReleaseSeatUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class ReleaseSeatUseCaseImpl implements ReleaseSeatUseCase {
	private final SeatRepositoryPort seatRepositoryPort;

	@Override
	public void releaseExpiredSeats(LocalDateTime threshold) {
		List<Seat> expiredSeats = seatRepositoryPort.findAllExpiredHeldSeats(threshold);

		List<Seat> releasedSeats = expiredSeats.stream()
			.map(Seat::release)
			.toList();

		seatRepositoryPort.saveAll(releasedSeats);
	}
}
