package com.kimgyeong.ticketingsimulation.user.adapter.in.web;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.kimgyeong.ticketingsimulation.global.controller.AbstractControllerTest;
import com.kimgyeong.ticketingsimulation.global.security.annotation.WithMockCustomUser;
import com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto.RegisterUserRequest;
import com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto.UpdateUserRequest;
import com.kimgyeong.ticketingsimulation.user.application.port.in.DeleteUserUseCase;
import com.kimgyeong.ticketingsimulation.user.application.port.in.ReadUserUseCase;
import com.kimgyeong.ticketingsimulation.user.application.port.in.RegisterUserUseCase;
import com.kimgyeong.ticketingsimulation.user.application.port.in.UpdateUserUseCase;
import com.kimgyeong.ticketingsimulation.user.domain.model.Role;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest extends AbstractControllerTest {
	@MockitoBean
	RegisterUserUseCase registerUserUseCase;

	@MockitoBean
	UpdateUserUseCase updateUserUseCase;

	@MockitoBean
	ReadUserUseCase readUserUseCase;

	@MockitoBean
	DeleteUserUseCase deleteUserUseCase;

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

	@Test
	@WithMockCustomUser(email = "hong@test.com")
	void getUser() throws Exception {
		User user = new User(1L, "홍길동", "hong@test.com", "encoded-password", "01012345678", Set.of(Role.ROLE_USER),
			LocalDateTime.now());

		given(readUserUseCase.read("hong@test.com"))
			.willReturn(user);

		mockMvc.perform(get("/users/me")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.name").value("홍길동"))
			.andExpect(jsonPath("$.email").value("hong@test.com"))
			.andExpect(jsonPath("$.phoneNumber").value("01012345678"));
	}

	@Test
	@WithMockCustomUser(email = "hong@test.com")
	void updateUser() throws Exception {
		UpdateUserRequest updateRequest = new UpdateUserRequest("테스트", "01099999999");
		User updatedUser = new User(1L, "테스트", "hong@test.com", "encoded-password", "01099999999",
			Set.of(Role.ROLE_USER),
			LocalDateTime.now());

		given(updateUserUseCase.update("hong@test.com", updateRequest.toCommand()))
			.willReturn(updatedUser);

		mockMvc.perform(patch("/users/me")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.name").value("테스트"))
			.andExpect(jsonPath("$.email").value("hong@test.com"))
			.andExpect(jsonPath("$.phoneNumber").value("01099999999"));
	}

	@Test
	@WithMockCustomUser(email = "hong@test.com")
	void deleteUser() throws Exception {
		willDoNothing().given(deleteUserUseCase).delete("hong@test.com");

		mockMvc.perform(delete("/users/me"))
			.andExpect(status().isNoContent());
	}
}