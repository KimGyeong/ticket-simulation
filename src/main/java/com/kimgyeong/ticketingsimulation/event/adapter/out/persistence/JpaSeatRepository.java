package com.kimgyeong.ticketingsimulation.event.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.entity.SeatEntity;

public interface JpaSeatRepository extends JpaRepository<SeatEntity, Long> {
	List<SeatEntity> findAllByEventId(Long eventId);

	@Query("SELECT s "
		+ "FROM Seat s "
		+ "WHERE s.status = 'TEMPORARY_HOLD' "
		+ "AND s.heldAt < :threshold")
	List<SeatEntity> findAllExpiredHeldSeats(LocalDateTime threshold);

	@Query("Select COUNT(s) "
		+ "FROM Seat s "
		+ "WHERE s.eventId = :id "
		+ "AND s.status = 'AVAILABLE'")
	Long countAvailableByEventId(Long id);
}
