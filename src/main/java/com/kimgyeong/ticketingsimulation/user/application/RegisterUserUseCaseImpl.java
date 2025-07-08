package com.kimgyeong.ticketingsimulation.user.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimgyeong.ticketingsimulation.global.exception.DuplicateEmailException;
import com.kimgyeong.ticketingsimulation.user.application.port.in.RegisterUserUseCase;
import com.kimgyeong.ticketingsimulation.user.application.port.in.command.RegisterUserCommand;
import com.kimgyeong.ticketingsimulation.user.application.port.out.UserRepositoryPort;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {
	private final UserRepositoryPort userRepositoryPort;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public Long register(RegisterUserCommand command) {
		if (userRepositoryPort.existsByEmail(command.email())) {
			throw new DuplicateEmailException();
		}

		String encodedPassword = passwordEncoder.encode(command.password());

		User user = User.withEncodedPassword(command.name(), command.email(), encodedPassword, command.phoneNumber());
		User savedUser = userRepositoryPort.save(user);

		return savedUser.id();
	}
}
