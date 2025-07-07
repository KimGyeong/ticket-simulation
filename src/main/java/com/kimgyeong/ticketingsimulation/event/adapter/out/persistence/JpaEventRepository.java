package com.kimgyeong.ticketingsimulation.event.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.entity.EventEntity;

public interface JpaEventRepository extends JpaRepository<EventEntity, Long> {
}
