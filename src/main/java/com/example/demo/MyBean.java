package com.example.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyBean {

	private final Log log = LogFactory.getLog(MyBean.class);

	private final AmqpAdmin amqpAdmin;
	private final AmqpTemplate amqpTemplate;

	@Autowired
	public MyBean(AmqpAdmin amqpAdmin, AmqpTemplate amqpTemplate) {
		this.amqpAdmin = amqpAdmin;
		this.amqpTemplate = amqpTemplate;

		this.log.info(this.amqpAdmin);
		this.log.info(this.amqpTemplate);

		// declare queue
		final Queue queue = new Queue("dev");
		this.amqpAdmin.declareQueue(queue);

		// declare binding
		final DirectExchange exchange = (DirectExchange) ExchangeBuilder
				.directExchange("amq.direct")
				.build();
		final Binding binding = BindingBuilder
				.bind(queue)
				.to(exchange)
				.withQueueName();
		this.amqpAdmin.declareBinding(binding);

		// send
		this.amqpTemplate.convertAndSend(
				exchange.getName(),
				queue.getName(),
				"test");

	}
}
