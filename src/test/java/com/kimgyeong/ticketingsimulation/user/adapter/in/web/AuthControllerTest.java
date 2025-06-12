package com.kimgyeong.ticketingsimulation.user.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.kimgyeong.ticketingsimulation.global.controller.AbstractControllerTest;
import com.kimgyeong.ticketingsimulation.global.exception.InvalidCredentialsException;
import com.kimgyeong.ticketingsimulation.global.exception.UserNotFoundException;
import com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto.LoginUserRequest;
import com.kimgyeong.ticketingsimulation.user.application.port.in.LoginUserUseCase;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest extends AbstractControllerTest {
	@MockitoBean
	private LoginUserUseCase loginUserUseCase;

	@Test
	void login() throws Exception {
		LoginUserRequest request = new LoginUserRequest("test@test.com", "password123!");
		String token = "mocked-jwt-token";

		given(loginUserUseCase.login(request.toCommand()))
			.willReturn(token);

		mockMvc.perform(post("/api/auth/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").value("mocked-jwt-token"));
	}

	@Test
	void login_InvalidEmail() throws Exception {
		LoginUserRequest request = new LoginUserRequest("invalid-email", "password123!");

		mockMvc.perform(post("/api/auth/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
			.andExpect(jsonPath("$.message").exists());
	}

	@Test
	void login_InvalidPassword() throws Exception {
		LoginUserRequest request = new LoginUserRequest("test@test.com", "");

		mockMvc.perform(post("/api/auth/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
			.andExpect(jsonPath("$.message").exists());
	}

	@Test
	void login_UserNotFound() throws Exception {
		LoginUserRequest request = new LoginUserRequest("notfound@test.com", "password123!");

		given(loginUserUseCase.login(request.toCommand()))
			.willThrow(new UserNotFoundException());

		mockMvc.perform(post("/api/auth/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value("E002"))
			.andExpect(jsonPath("$.message").exists());
	}

	@Test
	void login_InvalidCredentials() throws Exception {
		LoginUserRequest request = new LoginUserRequest("test@test.com", "wrongPassword");

		given(loginUserUseCase.login(request.toCommand()))
			.willThrow(new InvalidCredentialsException());

		mockMvc.perform(post("/api/auth/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.code").value("E003"))
			.andExpect(jsonPath("$.message").exists());
	}
}