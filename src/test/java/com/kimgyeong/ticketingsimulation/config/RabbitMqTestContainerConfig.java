package com.kimgyeong.ticketingsimulation.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class RabbitMqTestContainerConfig {
	static final GenericContainer<?> RABBIT_MQ = new GenericContainer<>(
		DockerImageName.parse("rabbitmq:3.12-management"))
		.withExposedPorts(5672, 15672);

	static {
		RABBIT_MQ.start();
	}

	@DynamicPropertySource
	static void overrideRabbitMqProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.rabbitmq.host", RABBIT_MQ::getHost);
		registry.add("spring.rabbitmq.port", () -> RABBIT_MQ.getMappedPort(5672));
	}
}
