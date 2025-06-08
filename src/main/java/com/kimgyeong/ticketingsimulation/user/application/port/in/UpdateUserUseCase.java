package com.kimgyeong.ticketingsimulation.user.application.port.in;

import com.kimgyeong.ticketingsimulation.user.application.port.in.command.UpdateUserCommand;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

public interface UpdateUserUseCase {
	User update(String email, UpdateUserCommand command);
}
