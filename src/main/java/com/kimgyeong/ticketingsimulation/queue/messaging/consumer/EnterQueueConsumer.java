package com.kimgyeong.ticketingsimulation.queue.messaging.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.kimgyeong.ticketingsimulation.queue.application.port.in.EnterQueueUseCase;
import com.kimgyeong.ticketingsimulation.queue.messaging.config.QueueNames;
import com.kimgyeong.ticketingsimulation.queue.messaging.message.EnterQueueMessage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Slf4j
public class EnterQueueConsumer {
	private final EnterQueueUseCase enterQueueUseCase;

	@RabbitListener(queues = QueueNames.ENTER_QUEUE)
	public void handle(EnterQueueMessage message) {
		log.info("Received QueueEntryAllowMessage: userId={}, eventId={}", message.userId(), message.eventId());
		enterQueueUseCase.enter(message.userId(), message.eventId());
	}
}
