package com.kimgyeong.ticketingsimulation.user.application.port.in;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.global.exception.UserNotFoundException;
import com.kimgyeong.ticketingsimulation.user.application.DeleteUserUseCaseImpl;
import com.kimgyeong.ticketingsimulation.user.application.port.out.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

	@InjectMocks
	private DeleteUserUseCaseImpl deleteUserUseCase;

	@Mock
	private UserRepositoryPort userRepositoryPort;

	@Test
	void deleteByEmail() {
		String email = "test@test.com";
		given(userRepositoryPort.existsByEmail(email)).willReturn(true);

		deleteUserUseCase.delete(email);

		then(userRepositoryPort).should().deleteByEmail(email);
	}

	@Test
	void deleteByEmail_whenUserNotExist() {
		String email = "test@test.com";
		given(userRepositoryPort.existsByEmail(email)).willReturn(false);

		assertThrows(UserNotFoundException.class, () -> deleteUserUseCase.delete(email));
	}
}