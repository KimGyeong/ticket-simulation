package com.kimgyeong.ticketingsimulation.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kimgyeong.ticketingsimulation.global.config.security.JwtTokenProvider;
import com.kimgyeong.ticketingsimulation.global.exception.InvalidCredentialsException;
import com.kimgyeong.ticketingsimulation.global.exception.UserNotFoundException;
import com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto.LoginResponse;
import com.kimgyeong.ticketingsimulation.user.application.port.in.command.LoginUserCommand;
import com.kimgyeong.ticketingsimulation.user.application.port.out.UserRepositoryPort;
import com.kimgyeong.ticketingsimulation.user.domain.model.Role;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseImplTest {

	@InjectMocks
	private LoginUserUseCaseImpl loginUserUseCase;

	@Mock
	private UserRepositoryPort userRepositoryPort;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Test
	void login() {
		// given
		String email = "test@test.com";
		String rawPassword = "Test1234!";
		String encodedPassword = "$2a$10$encoded"; // fake bcrypt
		Long userId = 1L;

		User user = new User(userId, "테스터", email, encodedPassword, "01012345678", Set.of(Role.ROLE_USER),
			LocalDateTime.now());

		given(userRepositoryPort.findByEmail(email)).willReturn(Optional.of(user));
		given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);
		given(jwtTokenProvider.generateToken(String.valueOf(userId), List.of("ROLE_USER"))).willReturn(
			"jwt.token.here");

		LoginUserCommand command = new LoginUserCommand(email, rawPassword);

		// when
		LoginResponse response = loginUserUseCase.login(command);

		// then
		assertThat(response.token()).isEqualTo("jwt.token.here");
	}

	@Test
	void login_emailNotExists() {
		// given
		LoginUserCommand command = new LoginUserCommand("notfound@test.com", "password");
		given(userRepositoryPort.findByEmail(command.email())).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> loginUserUseCase.login(command))
			.isInstanceOf(UserNotFoundException.class);
	}

	@Test
	void login_passwordIncorrect() {
		// given
		User user = new User(1L, "테스터", "test@test.com", "$2a$10$encoded", "01012345678", Set.of(Role.ROLE_USER),
			LocalDateTime.now());
		LoginUserCommand command = new LoginUserCommand("test@test.com", "wrong-password");

		given(userRepositoryPort.findByEmail(command.email())).willReturn(Optional.of(user));
		given(passwordEncoder.matches(command.password(), user.password())).willReturn(false);

		// when & then
		assertThatThrownBy(() -> loginUserUseCase.login(command))
			.isInstanceOf(InvalidCredentialsException.class);
	}
}