package com.kimgyeong.ticketingsimulation.user.application;

import org.springframework.stereotype.Service;

import com.kimgyeong.ticketingsimulation.global.exception.UserNotFoundException;
import com.kimgyeong.ticketingsimulation.user.application.port.in.UpdateUserUseCase;
import com.kimgyeong.ticketingsimulation.user.application.port.in.command.UpdateUserCommand;
import com.kimgyeong.ticketingsimulation.user.application.port.out.UserRepositoryPort;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
	private final UserRepositoryPort userRepositoryPort;

	@Override
	public User update(String email, UpdateUserCommand command) {
		User user = userRepositoryPort.findByEmail(email)
			.orElseThrow(UserNotFoundException::new);

		User updatedUser = user.update(command.name(), command.phoneNumber());
		return userRepositoryPort.save(updatedUser);
	}
}
