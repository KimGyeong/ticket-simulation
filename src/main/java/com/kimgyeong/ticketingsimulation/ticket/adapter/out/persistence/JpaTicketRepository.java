package com.kimgyeong.ticketingsimulation.ticket.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kimgyeong.ticketingsimulation.ticket.adapter.out.persistence.entity.TicketEntity;

public interface JpaTicketRepository extends JpaRepository<TicketEntity, Long> {
}
