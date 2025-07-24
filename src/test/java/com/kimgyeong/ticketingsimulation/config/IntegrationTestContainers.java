package com.kimgyeong.ticketingsimulation.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class IntegrationTestContainers {

	static final GenericContainer<?> REDIS = new GenericContainer<>(DockerImageName.parse("redis:7.2.5"))
		.withExposedPorts(6379);

	static final GenericContainer<?> RABBIT_MQ = new GenericContainer<>(
		DockerImageName.parse("rabbitmq:3.12-management"))
		.withExposedPorts(5672, 15672);

	static {
		REDIS.start();
		RABBIT_MQ.start();
	}

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", REDIS::getHost);
		registry.add("spring.redis.port", () -> REDIS.getMappedPort(6379));

		registry.add("spring.rabbitmq.host", RABBIT_MQ::getHost);
		registry.add("spring.rabbitmq.port", () -> RABBIT_MQ.getMappedPort(5672));
	}
}