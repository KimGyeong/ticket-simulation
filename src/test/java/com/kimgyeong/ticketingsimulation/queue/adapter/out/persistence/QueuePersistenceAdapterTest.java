package com.kimgyeong.ticketingsimulation.queue.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.kimgyeong.ticketingsimulation.config.RedisTestContainerConfig;
import com.kimgyeong.ticketingsimulation.queue.application.port.out.QueueRepositoryPort;
import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

@SpringBootTest
class QueuePersistenceAdapterTest extends RedisTestContainerConfig {
	@Autowired
	StringRedisTemplate redisTemplate;

	private QueueRepositoryPort queueRepositoryPort;

	private QueueEntry entry;

	@BeforeEach
	void setUp() {
		queueRepositoryPort = new QueuePersistenceAdapter(redisTemplate);
		entry = new QueueEntry(1L, 100L, LocalDateTime.of(2025, 1, 1, 0, 0));
	}

	@Test
	void enterQueue() {
		queueRepositoryPort.enterQueue(entry);
		assertThat(queueRepositoryPort.getUserRank(entry)).isEqualTo(0L);
	}
}