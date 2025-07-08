package com.kimgyeong.ticketingsimulation.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimgyeong.ticketingsimulation.global.exception.UserNotFoundException;
import com.kimgyeong.ticketingsimulation.user.application.port.in.DeleteUserUseCase;
import com.kimgyeong.ticketingsimulation.user.application.port.out.UserRepositoryPort;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
	private final UserRepositoryPort userRepositoryPort;

	@Override
	@Transactional
	public void delete(String email) {
		if (!userRepositoryPort.existsByEmail(email)) {
			throw new UserNotFoundException();
		}

		userRepositoryPort.deleteByEmail(email);
	}
}
