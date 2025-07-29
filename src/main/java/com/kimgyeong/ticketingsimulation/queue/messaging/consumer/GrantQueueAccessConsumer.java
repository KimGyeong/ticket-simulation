package com.kimgyeong.ticketingsimulation.queue.messaging.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.kimgyeong.ticketingsimulation.queue.application.port.in.GrantQueueAccessUseCase;
import com.kimgyeong.ticketingsimulation.queue.messaging.config.QueueNames;
import com.kimgyeong.ticketingsimulation.queue.messaging.message.GrantQueueAccessMessage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Slf4j
public class GrantQueueAccessConsumer {
	private final GrantQueueAccessUseCase grantQueueAccessUseCase;

	@RabbitListener(queues = QueueNames.GRANT_ACCESS_QUEUE)
	public void handle(GrantQueueAccessMessage message) {
		log.info("Received message to grant access: userId={}, eventId={}", message.userId(), message.eventId());
		grantQueueAccessUseCase.grantAccess(message.userId(), message.eventId());
	}
}
