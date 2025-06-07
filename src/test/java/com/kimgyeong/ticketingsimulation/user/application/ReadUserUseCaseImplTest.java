package com.kimgyeong.ticketingsimulation.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.global.exception.UserNotFoundException;
import com.kimgyeong.ticketingsimulation.user.application.port.out.UserRepositoryPort;
import com.kimgyeong.ticketingsimulation.user.domain.model.Role;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

@ExtendWith(MockitoExtension.class)
class ReadUserUseCaseImplTest {

	@InjectMocks
	ReadUserUseCaseImpl readUserUseCase;

	@Mock
	UserRepositoryPort userRepositoryPort;

	@Test
	void read_success() {
		String email = "test@example.com";
		User user = new User(1L, "홍길동", email, "password123!", "01012345678", Set.of(Role.ROLE_USER),
			LocalDateTime.now());
		given(userRepositoryPort.findByEmail(email)).willReturn(Optional.of(user));

		User result = readUserUseCase.read(email);

		assertThat(result.id()).isEqualTo(user.id());
	}

	@Test
	void read_userNotFound() {
		String email = "notfound@example.com";
		given(userRepositoryPort.findByEmail(email)).willReturn(Optional.empty());

		assertThatThrownBy(() -> readUserUseCase.read(email))
			.isInstanceOf(UserNotFoundException.class);
	}

}