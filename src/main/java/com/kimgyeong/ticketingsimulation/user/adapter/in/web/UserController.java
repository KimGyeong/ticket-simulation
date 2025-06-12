package com.kimgyeong.ticketingsimulation.user.adapter.in.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kimgyeong.ticketingsimulation.global.exception.UnauthenticatedException;
import com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto.RegisterUserRequest;
import com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto.UpdateUserRequest;
import com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto.UserResponse;
import com.kimgyeong.ticketingsimulation.user.application.port.in.DeleteUserUseCase;
import com.kimgyeong.ticketingsimulation.user.application.port.in.ReadUserUseCase;
import com.kimgyeong.ticketingsimulation.user.application.port.in.RegisterUserUseCase;
import com.kimgyeong.ticketingsimulation.user.application.port.in.UpdateUserUseCase;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Tag(name = "User API", description = "회원 관련 API")
public class UserController {
	private final RegisterUserUseCase registerUserUseCase;
	private final ReadUserUseCase readUserUseCase;
	private final UpdateUserUseCase updateUserUseCase;
	private final DeleteUserUseCase deleteUserUseCase;

	@Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "생성 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 이메일")
	})
	@PostMapping
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterUserRequest request) {
		Long id = registerUserUseCase.register(request.toCommand());
		return ResponseEntity.created(URI.create("/users/" + id)).build();
	}

	@Operation(summary = "회원 정보 조회", description = "현재 로그인한 사용자의 정보를 반환합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 이메일")
	})
	@GetMapping("/me")
	public ResponseEntity<UserResponse> getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new UnauthenticatedException();
		}

		String email = authentication.getName();
		User user = readUserUseCase.read(email);

		return ResponseEntity.ok(UserResponse.from(user));
	}

	@Operation(summary = "회원 정보 수정", description = "현재 로그인한 사용자의 이름과 전화번호를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "수정 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
	})
	@PatchMapping("/me")
	public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new UnauthenticatedException();
		}

		String email = authentication.getName();
		User updatedUser = updateUserUseCase.update(email, request.toCommand());

		return ResponseEntity.ok(UserResponse.from(updatedUser));
	}

	@Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자의 계정을 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "삭제 성공"),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
	})
	@DeleteMapping("/me")
	public ResponseEntity<Void> deleteUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new UnauthenticatedException();
		}

		String email = authentication.getName();
		deleteUserUseCase.delete(email);

		return ResponseEntity.noContent().build();
	}
}
