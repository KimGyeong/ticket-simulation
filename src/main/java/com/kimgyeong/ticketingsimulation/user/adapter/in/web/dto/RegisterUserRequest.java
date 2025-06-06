package com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto;

import com.kimgyeong.ticketingsimulation.user.application.port.in.command.RegisterUserCommand;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
	@NotBlank(message = "이름은 필수입니다.")
	String name,

	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	String email,

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?]).+$",
		message = "비밀번호는 알파벳, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다."
	)
	String password,

	@NotBlank(message = "전화번호는 필수입니다.")
	@Size(min = 11, max = 11, message = "전화번호는 11자리여야 합니다.")
	@Digits(integer = 11, fraction = 0, message = "전화번호는 숫자로만 이루어져야 합니다.")
	String phoneNumber
) {
	public RegisterUserCommand toCommand() {
		return new RegisterUserCommand(name, email, password, phoneNumber);
	}
}
