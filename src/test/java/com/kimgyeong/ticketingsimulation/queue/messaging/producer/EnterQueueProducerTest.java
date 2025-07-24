package com.kimgyeong.ticketingsimulation.queue.messaging.producer;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.kimgyeong.ticketingsimulation.queue.messaging.config.QueueNames;
import com.kimgyeong.ticketingsimulation.queue.messaging.message.EnterQueueMessage;

@ExtendWith(MockitoExtension.class)
class EnterQueueProducerTest {
	@Mock
	private RabbitTemplate rabbitTemplate;

	@InjectMocks
	private EnterQueueProducer producer;

	@Test
	void send() {
		EnterQueueMessage message = new EnterQueueMessage(1L, 100L, LocalDateTime.now());
		producer.send(message);

		verify(rabbitTemplate).convertAndSend(
			QueueNames.ENTER_EXCHANGE,
			QueueNames.ENTER_ROUTING_KEY,
			message
		);
	}

}