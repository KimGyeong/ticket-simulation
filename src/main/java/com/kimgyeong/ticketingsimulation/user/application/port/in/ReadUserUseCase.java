package com.kimgyeong.ticketingsimulation.user.application.port.in;

import com.kimgyeong.ticketingsimulation.user.domain.model.User;

public interface ReadUserUseCase {
	User read(String email);
}
