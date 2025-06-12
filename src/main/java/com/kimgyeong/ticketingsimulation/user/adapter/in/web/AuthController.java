package com.kimgyeong.ticketingsimulation.user.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto.LoginResponse;
import com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto.LoginUserRequest;
import com.kimgyeong.ticketingsimulation.user.application.port.in.LoginUserUseCase;
import com.kimgyeong.ticketingsimulation.user.application.port.in.command.LoginUserCommand;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Tag(name = "Auth API", description = "인증 관련 API")
public class AuthController {
	private final LoginUserUseCase loginUserUseCase;

	@Operation(summary = "로그인", description = "")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "401", description = "비밀번호 불일치"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 이메일")
	})
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginUserRequest request) {
		LoginUserCommand command = request.toCommand();
		String token = loginUserUseCase.login(command);
		return ResponseEntity.ok(new LoginResponse(token));
	}
}
