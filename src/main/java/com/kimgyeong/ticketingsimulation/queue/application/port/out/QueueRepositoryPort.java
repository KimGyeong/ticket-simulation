package com.kimgyeong.ticketingsimulation.queue.application.port.out;

import java.util.List;

import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

public interface QueueRepositoryPort {
	void enterQueue(QueueEntry entry);

	Long getUserRank(QueueEntry entry);

	boolean hasAccess(QueueEntry entry);

	void grantAccess(QueueEntry entry);

	int countGrantedUsers(Long eventId);

	List<QueueEntry> getTopEntries(Long eventId, long count);

	void removeFromAccessQueue(Long eventId, Long userId);
}
