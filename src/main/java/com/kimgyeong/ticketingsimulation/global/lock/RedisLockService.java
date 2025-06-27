package com.kimgyeong.ticketingsimulation.global.lock;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import com.kimgyeong.ticketingsimulation.global.exception.LockAcquisitionException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class RedisLockService {
	private final RedissonClient redissonClient;

	public <T> T runWithLock(String key, int waitTime, int leaseTime, Callable<T> task) {
		RLock lock = redissonClient.getLock(key);

		try {
			if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
				return task.call();
			} else {
				throw new LockAcquisitionException();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}
