package com.kimgyeong.ticketingsimulation.user.application.port.in;

import com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto.LoginResponse;
import com.kimgyeong.ticketingsimulation.user.application.port.in.command.LoginUserCommand;

public interface LoginUserUseCase {
	LoginResponse login(LoginUserCommand command);
}
