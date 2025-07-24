package com.kimgyeong.ticketingsimulation.queue.messaging.consumer;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.queue.application.port.in.EnterQueueUseCase;
import com.kimgyeong.ticketingsimulation.queue.messaging.message.EnterQueueMessage;

@ExtendWith(MockitoExtension.class)
class EnterQueueConsumerTest {
	@Mock
	private EnterQueueUseCase enterQueueUseCase;

	@InjectMocks
	private EnterQueueConsumer consumer;

	@Test
	void handle() {
		EnterQueueMessage message = new EnterQueueMessage(1L, 100L, LocalDateTime.now());

		consumer.handle(message);

		verify(enterQueueUseCase).enter(message.userId(), message.eventId());
	}
}