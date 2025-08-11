package com.kimgyeong.ticketingsimulation.queue.adapter.in;

import static org.mockito.BDDMockito.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.queue.application.port.out.QueueRepositoryPort;
import com.kimgyeong.ticketingsimulation.queue.domain.QueueEntry;

@ExtendWith(MockitoExtension.class)
class GrantQueueAccessSchedulerTest {

	@Mock
	private EventRepositoryPort eventRepositoryPort;

	@Mock
	private QueueRepositoryPort queueRepositoryPort;

	private Clock fixedClock;

	private GrantQueueAccessScheduler scheduler;

	@BeforeEach
	void setUp() {
		LocalDateTime fixedTime = LocalDateTime.of(2025, 7, 2, 12, 0, 0);
		fixedClock = Clock.fixed(fixedTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
		scheduler = new GrantQueueAccessScheduler(eventRepositoryPort, queueRepositoryPort, fixedClock);
	}

	@Test
	void grantQueueAccess() {
		Event event1 = new Event(1L, "Concert A", "Description", "Image URL", LocalDateTime.now().minusMinutes(1),
			LocalDateTime.now().plusMinutes(10), 1000, 100000L, 1L);
		Event event2 = new Event(2L, "Concert B", "Description", "Image URL", LocalDateTime.now().minusMinutes(2),
			LocalDateTime.now().plusMinutes(8), 1000, 100000L, 1L);

		when(eventRepositoryPort.findEventsInTicketingPeriod(any(LocalDateTime.class)))
			.thenReturn(List.of(event1, event2));

		List<QueueEntry> event1Entries = List.of(
			new QueueEntry(11L, 1L, LocalDateTime.now().minusSeconds(10)),
			new QueueEntry(12L, 1L, LocalDateTime.now().minusSeconds(5))
		);
		List<QueueEntry> event2Entries = List.of(
			new QueueEntry(21L, 2L, LocalDateTime.now().minusSeconds(3))
		);

		when(queueRepositoryPort.getTopEntries(1L, 1000)).thenReturn(event1Entries);
		when(queueRepositoryPort.getTopEntries(2L, 1000)).thenReturn(event2Entries);

		scheduler.grantQueueAccess();

		verify(eventRepositoryPort).findEventsInTicketingPeriod(LocalDateTime.now(fixedClock));

		verify(queueRepositoryPort).getTopEntries(1L, 1000);
		verify(queueRepositoryPort).getTopEntries(2L, 1000);

		verify(queueRepositoryPort).grantAccess(event1Entries.get(0));
		verify(queueRepositoryPort).grantAccess(event1Entries.get(1));
		verify(queueRepositoryPort).grantAccess(event2Entries.get(0));
	}

	@Test
	void grantQueueAccess_whenEventNotExist() {
		given(eventRepositoryPort.findEventsInTicketingPeriod(any(LocalDateTime.class))).willReturn(List.of());

		scheduler.grantQueueAccess();

		verifyNoInteractions(queueRepositoryPort);
	}

	@Test
	void grantQueueAccess_whenEnterQueueEmpty() {
		Event event = new Event(1L, "Concert A", "Description", "Image URL", LocalDateTime.now().minusMinutes(1),
			LocalDateTime.now().plusMinutes(10), 1000, 10000L, 1L);
		given(eventRepositoryPort.findEventsInTicketingPeriod(any(LocalDateTime.class))).willReturn(List.of(event));
		given(queueRepositoryPort.getTopEntries(event.id(), 1000)).willReturn(List.of());

		scheduler.grantQueueAccess();

		verify(queueRepositoryPort, never()).grantAccess(any());
	}

	@Test
	void grantQueueAccess_whenAccessQueueFull() {
		Event event = new Event(1L, "Concert A", "Description", "Image URL", LocalDateTime.now().minusMinutes(1),
			LocalDateTime.now().plusMinutes(10), 1000, 10000L, 1L);
		given(eventRepositoryPort.findEventsInTicketingPeriod(any(LocalDateTime.class))).willReturn(List.of(event));
		given(queueRepositoryPort.countGrantedUsers(anyLong())).willReturn(1000);

		scheduler.grantQueueAccess();

		verify(eventRepositoryPort).findEventsInTicketingPeriod(any());
		verify(queueRepositoryPort).countGrantedUsers(event.id());
		verify(queueRepositoryPort, never()).getTopEntries(anyLong(), anyLong());
		verify(queueRepositoryPort, never()).grantAccess(any());
	}
}