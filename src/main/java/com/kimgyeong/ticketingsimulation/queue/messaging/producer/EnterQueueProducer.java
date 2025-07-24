package com.kimgyeong.ticketingsimulation.queue.messaging.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.kimgyeong.ticketingsimulation.queue.messaging.config.QueueNames;
import com.kimgyeong.ticketingsimulation.queue.messaging.message.EnterQueueMessage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class EnterQueueProducer {
	private final RabbitTemplate rabbitTemplate;

	public void send(EnterQueueMessage message) {
		rabbitTemplate.convertAndSend(
			QueueNames.ENTER_EXCHANGE,
			QueueNames.ENTER_ROUTING_KEY,
			message
		);
	}
}
