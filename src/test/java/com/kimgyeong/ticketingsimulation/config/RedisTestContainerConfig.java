package com.kimgyeong.ticketingsimulation.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class RedisTestContainerConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final GenericContainer<?> REDIS_CONTAINER;

	static {
		REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:7.2.5"))
			.withExposedPorts(6379);
		REDIS_CONTAINER.start(); // Redis 컨테이너 실행
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		String host = REDIS_CONTAINER.getHost();
		Integer port = REDIS_CONTAINER.getMappedPort(6379);

		// Spring 환경에 Redis 접속 정보 주입
		TestPropertyValues.of(
			"spring.redis.host=" + host,
			"spring.redis.port=" + port
		).applyTo(applicationContext.getEnvironment());
	}
}
