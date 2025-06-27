package com.kimgyeong.ticketingsimulation.global.lock;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.kimgyeong.ticketingsimulation.config.RedisTestContainerConfig;

@SpringBootTest
@ContextConfiguration(initializers = RedisTestContainerConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedisLockServiceTest {

	@Autowired
	private RedisLockService redisLockService;

	@Test
	void lock_success() {
		String key = "lock:test:success";
		StringBuilder result = new StringBuilder();

		redisLockService.runWithLock(key, 1, 3, () -> {
			result.append("locked");
			return null;
		});

		assertEquals("locked", result.toString());
	}
}