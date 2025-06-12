package com.kimgyeong.ticketingsimulation.event.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kimgyeong.ticketingsimulation.event.application.port.in.ReadSeatUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class ReadSeatUseCaseImpl implements ReadSeatUseCase {
	private final SeatRepositoryPort seatRepository;

	@Override
	public List<Seat> getSeatsByEventId(Long eventId) {
		return seatRepository.findAllByEventId(eventId);
	}
}
