package com.kimgyeong.ticketingsimulation.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kimgyeong.ticketingsimulation.global.exception.DuplicateEmailException;
import com.kimgyeong.ticketingsimulation.user.application.port.in.command.RegisterUserCommand;
import com.kimgyeong.ticketingsimulation.user.application.port.out.UserRepositoryPort;
import com.kimgyeong.ticketingsimulation.user.domain.model.Role;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseImplTest {
	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UserRepositoryPort port;

	private RegisterUserUseCaseImpl useCase;

	private RegisterUserCommand command;

	@BeforeEach
	void setUp() {
		useCase = new RegisterUserUseCaseImpl(port, passwordEncoder);
		command = new RegisterUserCommand("test", "test@test.com", "test", "12345678901");
	}

	@Test
	void register() {
		String encodedPassword = "encoded";

		User createdUser = User.withEncodedPassword(command.name(), command.email(), encodedPassword,
			command.phoneNumber());
		User savedUser = new User(1L, createdUser.name(), createdUser.email(), createdUser.password(),
			createdUser.phoneNumber(), Set.of(Role.ROLE_USER), LocalDateTime.now());

		when(passwordEncoder.encode(command.password())).thenReturn(encodedPassword);
		when(port.save(any(User.class))).thenReturn(savedUser);

		Long result = useCase.register(command);

		assertThat(result).isEqualTo(1L);
		verify(passwordEncoder).encode(command.password());
		verify(port).save(any(User.class));
	}

	@Test
	void register_duplicateEmail() {
		when(port.existsByEmail("test@test.com")).thenReturn(true);

		assertThrows(DuplicateEmailException.class, () -> useCase.register(command));
	}
}