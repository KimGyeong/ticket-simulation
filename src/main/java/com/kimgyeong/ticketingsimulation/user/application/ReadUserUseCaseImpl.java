package com.kimgyeong.ticketingsimulation.user.application;

import org.springframework.stereotype.Service;

import com.kimgyeong.ticketingsimulation.global.exception.UserNotFoundException;
import com.kimgyeong.ticketingsimulation.user.application.port.in.ReadUserUseCase;
import com.kimgyeong.ticketingsimulation.user.application.port.out.UserRepositoryPort;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class ReadUserUseCaseImpl implements ReadUserUseCase {
	private final UserRepositoryPort userRepositoryPort;

	@Override
	public User read(String email) {
		return userRepositoryPort.findByEmail(email)
			.orElseThrow(UserNotFoundException::new);
	}
}
