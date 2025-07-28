package com.kimgyeong.ticketingsimulation.event.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.entity.EventEntity;

public interface JpaEventRepository extends JpaRepository<EventEntity, Long> {
	@Query("SELECT e "
		+ "FROM Event e "
		+ "WHERE e.ticketingStartAt <= :now "
		+ "AND e.eventStartAt > :now")
	List<EventEntity> findEventsInTicketingPeriod(LocalDateTime now);
}
