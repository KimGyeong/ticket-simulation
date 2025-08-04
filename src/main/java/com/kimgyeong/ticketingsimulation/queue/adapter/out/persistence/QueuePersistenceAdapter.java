package com.kimgyeong.ticketingsimulation.queue.adapter.out.persistence;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
	public static final String ACCESS_KEY_ALL_FORMAT = "event:%d:access:*";

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
	public boolean hasAccess(Long eventId, Long userId) {
		String key = getAccessKey(eventId, userId);
		String value = redisTemplate.opsForValue().get(key);

		return Boolean.parseBoolean(value);
	}

	@Override
	public void grantAccess(QueueEntry entry) {
		String queueKey = getQueueKey(entry);
		String accessKey = getAccessKey(entry);

		redisTemplate.opsForValue().set(accessKey, TRUE);

		redisTemplate.opsForZSet().remove(queueKey, entry.userId().toString());
	}

	@Override
	public int countGrantedUsers(Long eventId) {
		String pattern = String.format(ACCESS_KEY_ALL_FORMAT, eventId);
		return redisTemplate.keys(pattern).stream()
			.filter(key -> TRUE.equals(redisTemplate.opsForValue().get(key)))
			.toList()
			.size();
	}

	@Override
	public List<QueueEntry> getTopEntries(Long eventId, long count) {
		String key = String.format(QUEUE_KEY_FORMAT, eventId);

		Set<String> userIds = redisTemplate.opsForZSet()
			.range(key, 0, count - 1);

		if (userIds == null || userIds.isEmpty()) {
			return Collections.emptyList();
		}

		return userIds.stream()
			.map(userIdString -> {
				Long userId = Long.valueOf(userIdString);
				Double score = redisTemplate.opsForZSet().score(key, userIdString);

				if (score == null) {
					return null;
				}

				LocalDateTime enteredAt = LocalDateTime.ofEpochSecond(score.longValue(), 0, ZoneOffset.UTC);
				return new QueueEntry(userId, eventId, enteredAt);
			})
			.filter(Objects::nonNull)
			.toList();
	}

	@Override
	public void removeFromAccessQueue(Long eventId, Long userId) {
		String key = getAccessKey(eventId, userId);
		redisTemplate.opsForZSet().remove(key, userId.toString());
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

	private String getAccessKey(Long eventId, Long userId) {
		return String.format(ACCESS_KEY_FORMAT, eventId, userId);
	}
}
