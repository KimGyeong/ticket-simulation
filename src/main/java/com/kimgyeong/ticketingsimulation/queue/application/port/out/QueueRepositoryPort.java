package com.kimgyeong.ticketingsimulation.queue.application.port.out;

import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

public interface QueueRepositoryPort {
	void enterQueue(QueueEntry entry);
	Long getUserRank(QueueEntry entry);

	boolean hasAccess(QueueEntry entry);
}
