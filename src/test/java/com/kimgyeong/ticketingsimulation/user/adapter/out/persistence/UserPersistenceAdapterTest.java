package com.kimgyeong.ticketingsimulation.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.user.adapter.out.entity.UserEntity;
import com.kimgyeong.ticketingsimulation.user.adapter.out.persistence.mapper.UserEntityMapper;
import com.kimgyeong.ticketingsimulation.user.domain.model.Role;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest {
	@Mock
	private JpaUserRepository repository;

	@InjectMocks
	private UserPersistenceAdapter adapter;

	@Test
	void save() {
		User user = new User(null, "test", "test@test.com", "test1234!", "12345678901", Set.of(Role.ROLE_USER),
			LocalDateTime.now());
		UserEntity entity = UserEntityMapper.toEntity(user);
		given(repository.save(any(UserEntity.class))).willReturn(entity);

		User result = adapter.save(user);

		assertThat(result.email()).isEqualTo(user.email());
	}

	@Test
	void findByEmail_whenFindByEmailExists() {
		String email = "test@test.com";
		UserEntity entity = new UserEntity(1L, "test", "test@test.com", "test1234!", "12345678901",
			Set.of(Role.ROLE_USER),
			LocalDateTime.now());
		given(repository.findByEmail(email)).willReturn(Optional.of(entity));

		Optional<User> result = adapter.findByEmail(email);

		assertThat(result).isPresent();
		assertThat(result.get().email()).isEqualTo(email);
	}

	@Test
	void findByEmail_whenFindByEmailNotExist() {
		String email = "test@test.com";
		given(repository.findByEmail(email)).willReturn(Optional.empty());

		Optional<User> result = adapter.findByEmail(email);

		assertThat(result).isEmpty();
	}

	@Test
	void findById_whenFindByIdExists() {
		long id = 1L;
		UserEntity entity = new UserEntity(id, "test", "test@test.com", "test1234!", "12345678901",
			Set.of(Role.ROLE_USER),
			LocalDateTime.now());
		given(repository.findById(id)).willReturn(Optional.of(entity));

		Optional<User> result = adapter.findById(id);

		assertThat(result).isPresent();
		assertThat(result.get().id()).isEqualTo(id);
	}

	@Test
	void findById_whenFindByIdNotExist() {
		long id = 1L;
		given(repository.findById(id)).willReturn(Optional.empty());

		Optional<User> result = adapter.findById(id);

		assertThat(result).isEmpty();
	}

	@Test
	void deleteByEmail() {
		String email = "test@test.com";

		adapter.deleteByEmail(email);

		verify(repository).deleteByEmail(email);
	}
}