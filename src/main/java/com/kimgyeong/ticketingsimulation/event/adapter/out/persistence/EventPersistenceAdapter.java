package com.kimgyeong.ticketingsimulation.event.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.entity.EventEntity;
import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.mapper.EventEntityMapper;
import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Repository
public class EventPersistenceAdapter implements EventRepositoryPort {
	private final JpaEventRepository eventRepository;

	@Override
	public Event save(Event event) {
		EventEntity entity = EventEntityMapper.toEntity(event);
		EventEntity savedEntity = eventRepository.save(entity);
		return EventEntityMapper.toDomain(savedEntity);
	}

	@Override
	public List<Event> findAll() {
		List<EventEntity> allEvents = eventRepository.findAll();
		return allEvents.stream()
			.map(EventEntityMapper::toDomain)
			.toList();
	}

	@Override
	public Optional<Event> findById(Long id) {
		return eventRepository.findById(id)
			.map(EventEntityMapper::toDomain);
	}

	@Override
	public void deleteById(Long id) {
		eventRepository.deleteById(id);
	}
}
