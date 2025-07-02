package com.kimgyeong.ticketingsimulation.event.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.entity.SeatEntity;
import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.mapper.SeatEntityMapper;
import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Component
public class SeatPersistenceAdapter implements SeatRepositoryPort {
	private final JpaSeatRepository repository;

	@Override
	public List<Seat> findAllByEventId(Long eventId) {
		List<SeatEntity> allByEventId = repository.findAllByEventId(eventId);

		return allByEventId.stream()
			.map(SeatEntityMapper::toDomain)
			.toList();
	}

	@Override
	public void save(Seat seat) {
		SeatEntity entity = SeatEntityMapper.toEntity(seat);

		repository.save(entity);
	}

	@Override
	public Optional<Seat> findById(Long seatId) {
		return repository.findById(seatId)
			.map(SeatEntityMapper::toDomain);
	}

	@Override
	public List<Seat> findAllExpiredHeldSeats(LocalDateTime threshold) {
		List<SeatEntity> allExpiredHeldSeats = repository.findAllExpiredHeldSeats(threshold);

		return allExpiredHeldSeats.stream()
			.map(SeatEntityMapper::toDomain)
			.toList();
	}

	@Override
	public void saveAll(List<Seat> seats) {
		List<SeatEntity> seatEntities = seats.stream()
			.map(SeatEntityMapper::toEntity)
			.toList();

		repository.saveAll(seatEntities);
	}
}
