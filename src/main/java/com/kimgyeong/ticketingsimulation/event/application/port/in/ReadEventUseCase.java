package com.kimgyeong.ticketingsimulation.event.application.port.in;

import com.kimgyeong.ticketingsimulation.event.application.model.EventDetailResult;

public interface ReadEventUseCase {
	EventDetailResult findById(Long id);
}
