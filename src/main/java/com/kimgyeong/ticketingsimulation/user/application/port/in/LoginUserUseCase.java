package com.kimgyeong.ticketingsimulation.user.application.port.in;

import com.kimgyeong.ticketingsimulation.user.application.port.in.command.LoginUserCommand;

public interface LoginUserUseCase {
	String login(LoginUserCommand command);
}
