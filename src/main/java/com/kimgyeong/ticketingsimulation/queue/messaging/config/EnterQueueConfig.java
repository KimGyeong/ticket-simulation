package com.kimgyeong.ticketingsimulation.queue.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnterQueueConfig {

	@Bean
	public Queue entryAllowQueue() {
		return new Queue(QueueNames.ENTER_QUEUE, true);
	}

	@Bean
	public DirectExchange entryAllowExchange() {
		return new DirectExchange(QueueNames.ENTER_EXCHANGE);
	}

	@Bean
	public Binding entryAllowBinding() {
		return BindingBuilder
			.bind(entryAllowQueue())
			.to(entryAllowExchange())
			.with(QueueNames.ENTER_ROUTING_KEY);
	}
}
