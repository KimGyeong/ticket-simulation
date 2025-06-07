package com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto;

import com.kimgyeong.ticketingsimulation.user.application.port.in.command.LoginUserCommand;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest(
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	String email,

	@NotBlank(message = "비밀번호는 필수입니다.")
	String password
) {
	public LoginUserCommand toCommand() {
		return new LoginUserCommand(email, password);
	}
}
