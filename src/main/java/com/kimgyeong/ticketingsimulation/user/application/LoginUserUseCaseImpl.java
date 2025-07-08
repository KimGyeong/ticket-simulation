package com.kimgyeong.ticketingsimulation.user.application;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimgyeong.ticketingsimulation.global.config.security.JwtTokenProvider;
import com.kimgyeong.ticketingsimulation.global.exception.InvalidCredentialsException;
import com.kimgyeong.ticketingsimulation.global.exception.UserNotFoundException;
import com.kimgyeong.ticketingsimulation.user.application.port.in.LoginUserUseCase;
import com.kimgyeong.ticketingsimulation.user.application.port.in.command.LoginUserCommand;
import com.kimgyeong.ticketingsimulation.user.application.port.out.UserRepositoryPort;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class LoginUserUseCaseImpl implements LoginUserUseCase {
	private final UserRepositoryPort userRepositoryPort;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	@Transactional(readOnly = true)
	public String login(LoginUserCommand command) {
		User user = userRepositoryPort.findByEmail(command.email())
			.orElseThrow(UserNotFoundException::new);

		if (!passwordEncoder.matches(command.password(), user.password())) {
			throw new InvalidCredentialsException();
		}

		List<String> roles = user.roles()
			.stream()
			.map(Enum::name)
			.toList();

		return jwtTokenProvider.generateToken(user.id(), user.email(), roles);
	}
}
