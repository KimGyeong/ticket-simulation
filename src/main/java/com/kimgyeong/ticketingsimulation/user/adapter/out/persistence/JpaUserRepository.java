package com.kimgyeong.ticketingsimulation.user.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kimgyeong.ticketingsimulation.user.adapter.out.entity.UserEntity;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByEmail(String email);

	boolean existsByEmail(String email);
}
