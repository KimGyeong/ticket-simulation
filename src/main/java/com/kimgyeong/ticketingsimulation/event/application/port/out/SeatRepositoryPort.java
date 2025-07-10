package com.kimgyeong.ticketingsimulation.event.application.port.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;

public interface SeatRepositoryPort {
	List<Seat> findAllByEventId(Long eventId);

	void save(Seat seat);

	Optional<Seat> findById(Long seatId);

	List<Seat> findAllExpiredHeldSeats(LocalDateTime threshold);

	void saveAll(List<Seat> seats);

	Long countAvailableByEventId(Long id);
}
