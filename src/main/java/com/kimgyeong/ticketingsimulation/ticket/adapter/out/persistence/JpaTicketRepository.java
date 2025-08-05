package com.kimgyeong.ticketingsimulation.ticket.adapter.out.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kimgyeong.ticketingsimulation.ticket.adapter.out.persistence.entity.TicketEntity;

public interface JpaTicketRepository extends JpaRepository<TicketEntity, Long> {
	List<TicketEntity> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
