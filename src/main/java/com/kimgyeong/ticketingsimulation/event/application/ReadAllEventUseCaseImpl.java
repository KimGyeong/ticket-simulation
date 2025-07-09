package com.kimgyeong.ticketingsimulation.event.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimgyeong.ticketingsimulation.event.application.port.in.ReadAllEventUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class ReadAllEventUseCaseImpl implements ReadAllEventUseCase {
	private final EventRepositoryPort eventRepositoryPort;

	@Override
	@Transactional(readOnly = true)
	public List<Event> findAll() {
		return eventRepositoryPort.findAll();
	}
}
