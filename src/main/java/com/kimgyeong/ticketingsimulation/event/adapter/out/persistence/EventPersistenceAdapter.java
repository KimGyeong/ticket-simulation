package com.kimgyeong.ticketingsimulation.event.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.entity.EventEntity;
import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.mapper.EventEntityMapper;
import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Component
public class EventPersistenceAdapter implements EventRepositoryPort {
	private JpaEventRepository eventRepository;

	@Override
	public Event save(Event event) {
		EventEntity entity = EventEntityMapper.toEntity(event);
		EventEntity savedEntity = eventRepository.save(entity);
		return EventEntityMapper.toDomain(savedEntity);
	}
}
