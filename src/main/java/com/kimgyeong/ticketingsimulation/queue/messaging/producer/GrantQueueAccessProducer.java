package com.kimgyeong.ticketingsimulation.queue.messaging.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.kimgyeong.ticketingsimulation.queue.messaging.config.QueueNames;
import com.kimgyeong.ticketingsimulation.queue.messaging.message.GrantQueueAccessMessage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class GrantQueueAccessProducer {
	private final RabbitTemplate rabbitTemplate;

	public void send(GrantQueueAccessMessage message) {
		rabbitTemplate.convertAndSend(
			QueueNames.GRANT_ACCESS_EXCHANGE,
			QueueNames.GRANT_ACCESS_ROUTING_KEY,
			message
		);
	}
}
