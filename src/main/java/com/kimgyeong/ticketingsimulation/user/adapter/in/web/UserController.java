package com.kimgyeong.ticketingsimulation.user.adapter.in.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto.RegisterUserRequest;
import com.kimgyeong.ticketingsimulation.user.application.port.in.RegisterUserUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Tag(name = "User API", description = "회원 관련 API")
public class UserController {
	private final RegisterUserUseCase registerUserUseCase;

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
}
