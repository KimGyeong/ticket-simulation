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

import com.kimgyeong.ticketingsimulation.user.application.port.in.command.UpdateUserCommand;
import com.kimgyeong.ticketingsimulation.user.application.port.out.UserRepositoryPort;
import com.kimgyeong.ticketingsimulation.user.domain.model.Role;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseImplTest {

	@InjectMocks
	private UpdateUserUseCaseImpl updateUserUseCase;

	@Mock
	private UserRepositoryPort userRepositoryPort;

	@Test
	void update() {
		String email = "test@test.com";
		User user = new User(1L, "테스터", "test@test.com", "$2a$10$encoded", "01012345678", Set.of(Role.ROLE_USER),
			LocalDateTime.now());

		given(userRepositoryPort.findByEmail(email)).willReturn(Optional.of(user));
		given(userRepositoryPort.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));

		UpdateUserCommand command = new UpdateUserCommand("홍길동", "01099999999");

		User result = updateUserUseCase.update(email, command);

		assertThat(result.name()).isEqualTo("홍길동");
		assertThat(result.phoneNumber()).isEqualTo("01099999999");
	}
}