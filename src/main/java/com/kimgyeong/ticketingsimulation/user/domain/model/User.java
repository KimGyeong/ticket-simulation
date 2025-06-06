package com.kimgyeong.ticketingsimulation.user.domain.model;

import java.time.LocalDateTime;
import java.util.Set;

public record User(Long id, String name, String email, String password, String phoneNumber, Set<Role> roles,
				   LocalDateTime createdAt) {
	public static User withEncodedPassword(String name, String email, String encodedPassword, String phoneNumber) {
		return new User(null, name, email, encodedPassword, phoneNumber, Set.of(Role.ROLE_USER), null);
	}
}
