package com.kimgyeong.ticketingsimulation.user.application.port.out;

import java.util.Optional;

import com.kimgyeong.ticketingsimulation.user.domain.model.User;

public interface UserRepositoryPort {
	User save(User user);

	Optional<User> findByEmail(String email);

	Optional<User> findById(Long id);

	boolean existsByEmail(String email);

	void deleteById(Long id);
}
