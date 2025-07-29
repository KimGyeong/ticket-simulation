package com.kimgyeong.ticketingsimulation.queue.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

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

	@Test
	void hasAccess() {
		String key = "event:100:access:1";
		redisTemplate.opsForValue().set(key, "true");
		QueueEntry entry1 = new QueueEntry(1L, 100L, null);

		assertThat(queueRepositoryPort.hasAccess(entry1)).isTrue();
	}

	@Test
	void hasAccess_whenAccessNotGranted_returnsFalse() {
		String key = "event:100:access:1";
		redisTemplate.delete(key);

		QueueEntry entry = new QueueEntry(1L, 100L, null);
		assertThat(queueRepositoryPort.hasAccess(entry)).isFalse();
	}

	@Test
	void grantAccess() {
		queueRepositoryPort.enterQueue(entry);

		queueRepositoryPort.grantAccess(entry);

		String accessKey = String.format("event:%d:access:%d", entry.eventId(), entry.userId());
		String queueKey = String.format("event:%d:queue", entry.eventId());

		String access = redisTemplate.opsForValue().get(accessKey);
		Long rank = redisTemplate.opsForZSet().rank(queueKey, entry.userId().toString());

		assertThat(access).isEqualTo("true");
		assertThat(rank).isNull();
	}

	@Test
	void getTopEntries() {
		Long eventId = 100L;

		QueueEntry entry1 = new QueueEntry(1L, eventId, LocalDateTime.of(2024, 1, 1, 10, 0));
		QueueEntry entry2 = new QueueEntry(2L, eventId, LocalDateTime.of(2024, 1, 1, 10, 5));
		QueueEntry entry3 = new QueueEntry(3L, eventId, LocalDateTime.of(2024, 1, 1, 10, 10));

		queueRepositoryPort.enterQueue(entry2);
		queueRepositoryPort.enterQueue(entry3);
		queueRepositoryPort.enterQueue(entry1);

		List<QueueEntry> topEntries = queueRepositoryPort.getTopEntries(eventId, 2);

		assertThat(topEntries).hasSize(2);
		assertThat(topEntries.get(0).userId()).isEqualTo(1L);
		assertThat(topEntries.get(1).userId()).isEqualTo(2L);
	}
}