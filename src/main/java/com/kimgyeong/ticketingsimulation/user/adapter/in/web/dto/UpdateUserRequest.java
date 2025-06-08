package com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto;

import com.kimgyeong.ticketingsimulation.user.application.port.in.command.UpdateUserCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateUserRequest(
	@NotBlank(message = "이름은 필수입니다.")
	String name,

	@NotBlank(message = "전화번호는 필수입니다.")
	@Pattern(regexp = "^\\d{11}$", message = "전화번호는 숫자로 이루어진 11자리여야 합니다.")
	String phoneNumber) {
	public UpdateUserCommand toCommand() {
		return new UpdateUserCommand(name, phoneNumber);
	}
}
