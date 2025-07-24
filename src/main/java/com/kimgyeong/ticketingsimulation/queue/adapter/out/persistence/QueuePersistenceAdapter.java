package com.kimgyeong.ticketingsimulation.queue.adapter.out.persistence;

import java.time.ZoneOffset;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.kimgyeong.ticketingsimulation.queue.application.port.out.QueueRepositoryPort;
import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Repository
public class QueuePersistenceAdapter implements QueueRepositoryPort {
	private static final String QUEUE_KEY_FORMAT = "event:%d:queue";
	private static final String ACCESS_KEY_FORMAT = "event:%d:access:%d";
	private static final String TRUE = "true";

	private final StringRedisTemplate redisTemplate;

	@Override
	public void enterQueue(QueueEntry entry) {
		String key = getQueueKey(entry);
		String member = getMemberKey(entry);

		double score = entry.enteredAt().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
		redisTemplate.opsForZSet().add(key, member, score);
	}

	@Override
	public Long getUserRank(QueueEntry entry) {
		String key = getQueueKey(entry);
		String member = getMemberKey(entry);
		return redisTemplate.opsForZSet().rank(key, member);
	}

	@Override
	public boolean hasAccess(QueueEntry entry) {
		String key = getAccessKey(entry);
		String value = redisTemplate.opsForValue().get(key);

		return Boolean.parseBoolean(value);
	}

	@Override
	public void grantAccess(QueueEntry entry) {
		String key = getAccessKey(entry);
		redisTemplate.opsForValue().set(key, TRUE);
	}

	private String getQueueKey(QueueEntry entry) {
		return String.format(QUEUE_KEY_FORMAT, entry.eventId());
	}

	private String getMemberKey(QueueEntry entry) {
		return String.valueOf(entry.userId());
	}

	private String getAccessKey(QueueEntry entry) {
		return String.format(ACCESS_KEY_FORMAT, entry.eventId(), entry.userId());
	}
}
