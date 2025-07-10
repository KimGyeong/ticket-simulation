package com.kimgyeong.ticketingsimulation.event.application.model;

import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

public record EventDetailResult(Event event, long availableSeatCount) {
}
