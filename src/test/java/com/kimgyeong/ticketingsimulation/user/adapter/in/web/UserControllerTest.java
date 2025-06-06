package com.kimgyeong.ticketingsimulation.user.adapter.in.web;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.kimgyeong.ticketingsimulation.global.controller.AbstractControllerTest;
import com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto.RegisterUserRequest;
import com.kimgyeong.ticketingsimulation.user.application.port.in.RegisterUserUseCase;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest extends AbstractControllerTest {
	@MockitoBean
	RegisterUserUseCase registerUserUseCase;

	private static Stream<Arguments> invalidRegisterRequests() {
		return Stream.of(
			Arguments.of("name", ""),
			Arguments.of("email", "not-an-email"),
			Arguments.of("password", "1234"),
			Arguments.of("phoneNumber", "abc")
		);
	}

	@Test
	void register() throws Exception {
		RegisterUserRequest registerUserRequest = new RegisterUserRequest("test", "test@test.com", "test1234!",
			"12345678901");

		when(registerUserUseCase.register(any())).thenReturn(1L);

		mockMvc.perform(post("/users")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerUserRequest)))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/users/1"));
	}

	@ParameterizedTest(name = "{index} - 필드: {0}")
	@MethodSource("invalidRegisterRequests")
	void registerValidationTest(String field, String value) throws Exception {
		RegisterUserRequest request = new RegisterUserRequest(
			field.equals("name") ? value : "정상이름",
			field.equals("email") ? value : "test@test.com",
			field.equals("password") ? value : "Test1234!",
			field.equals("phoneNumber") ? value : "01012345678"
		);

		mockMvc.perform(post("/users")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
			.andExpect(jsonPath("$.message").exists());
	}

}