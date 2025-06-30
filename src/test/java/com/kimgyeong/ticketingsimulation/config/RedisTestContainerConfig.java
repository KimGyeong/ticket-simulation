package com.kimgyeong.ticketingsimulation.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class RedisTestContainerConfig {

	static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:7.2.5"))
		.withExposedPorts(6379);

	static {
		REDIS_CONTAINER.start();
	}

	@DynamicPropertySource
	static void overrideRedisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
		registry.add("spring.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
	}
}
