package com.kimgyeong.ticketingsimulation.user.application.port.in;

import com.kimgyeong.ticketingsimulation.user.application.port.in.command.RegisterUserCommand;

public interface RegisterUserUseCase {
	Long register(RegisterUserCommand command);
}
