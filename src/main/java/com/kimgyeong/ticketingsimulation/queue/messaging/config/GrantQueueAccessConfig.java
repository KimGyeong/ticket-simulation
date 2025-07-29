package com.kimgyeong.ticketingsimulation.queue.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrantQueueAccessConfig {

	@Bean
	public Queue queue() {
		return new Queue(QueueNames.GRANT_ACCESS_QUEUE, true);
	}

	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(QueueNames.GRANT_ACCESS_EXCHANGE);
	}

	@Bean
	public Binding binding() {
		return BindingBuilder
			.bind(queue())
			.to(exchange())
			.with(QueueNames.GRANT_ACCESS_ROUTING_KEY);
	}
}
