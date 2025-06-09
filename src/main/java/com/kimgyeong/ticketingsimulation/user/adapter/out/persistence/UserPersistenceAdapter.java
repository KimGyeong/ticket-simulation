package com.kimgyeong.ticketingsimulation.user.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.kimgyeong.ticketingsimulation.user.adapter.out.persistence.entity.UserEntity;
import com.kimgyeong.ticketingsimulation.user.adapter.out.persistence.mapper.UserEntityMapper;
import com.kimgyeong.ticketingsimulation.user.application.port.out.UserRepositoryPort;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Component
public class UserPersistenceAdapter implements UserRepositoryPort {
	private final JpaUserRepository userRepository;

	@Override
	public User save(User user) {
		UserEntity userEntity = UserEntityMapper.toEntity(user);
		UserEntity savedUserEntity = userRepository.save(userEntity);
		return UserEntityMapper.toDomain(savedUserEntity);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email)
			.map(UserEntityMapper::toDomain);
	}

	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id)
			.map(UserEntityMapper::toDomain);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public void deleteByEmail(String email) {
		userRepository.deleteByEmail(email);
	}
}
