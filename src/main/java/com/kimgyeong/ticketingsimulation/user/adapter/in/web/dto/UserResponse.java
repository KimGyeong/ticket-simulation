package com.kimgyeong.ticketingsimulation.user.adapter.in.web.dto;

import com.kimgyeong.ticketingsimulation.user.domain.model.User;

public record UserResponse(Long id, String name, String email, String phoneNumber) {
	public static UserResponse from(User user) {
		return new UserResponse(
			user.id(),
			user.name(),
			user.email(),
			user.phoneNumber()
		);
	}
}
