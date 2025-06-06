package com.kimgyeong.ticketingsimulation.user.adapter.out.persistence.mapper;

import com.kimgyeong.ticketingsimulation.user.adapter.out.entity.UserEntity;
import com.kimgyeong.ticketingsimulation.user.domain.model.User;

public class UserEntityMapper {
	public static User toDomain(UserEntity entity) {
		return new User(
			entity.getId(),
			entity.getName(),
			entity.getEmail(),
			entity.getPassword(),
			entity.getPhoneNumber(),
			entity.getRoles(),
			entity.getCreatedAt()
		);
	}

	public static UserEntity toEntity(User user) {
		return new UserEntity(
			user.id(),
			user.name(),
			user.email(),
			user.password(),
			user.phoneNumber(),
			user.roles(),
			user.createdAt()
		);
	}
}
