package com.example.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
		final Queue queue1 = new Queue("queue1");
		this.amqpAdmin.declareQueue(queue1);

		final Queue queue2 = new Queue("queue2");
		this.amqpAdmin.declareQueue(queue2);

		// declare exchange
		final TopicExchange exchange = (TopicExchange) ExchangeBuilder
				.topicExchange("amq.topic")
				.build();

		// declare binding
		final Binding binding1 = BindingBuilder
				.bind(queue1)
				.to(exchange)
				.with("*.orange.*");
		this.amqpAdmin.declareBinding(binding1);

		final Binding binding2 = BindingBuilder
				.bind(queue2)
				.to(exchange)
				.with("*.*.rabbit");
		this.amqpAdmin.declareBinding(binding2);

		final Binding binding3 = BindingBuilder
				.bind(queue2)
				.to(exchange)
				.with("lazy.#");
		this.amqpAdmin.declareBinding(binding3);

		// send
		this.amqpTemplate.convertAndSend(
				exchange.getName(),
				"quick.orange.rabbit",
				"test message 1");
		this.amqpTemplate.convertAndSend(
				exchange.getName(),
				"lazy.orange.elephant",
				"test message 2");
		this.amqpTemplate.convertAndSend(
				exchange.getName(),
				"quick.orange.fox",
				"test message 3");
		this.amqpTemplate.convertAndSend(
				exchange.getName(),
				"lazy.brown.fox",
				"test message 4");
		this.amqpTemplate.convertAndSend(
				exchange.getName(),
				"lazy.pink.rabbit",
				"test message 5");
		this.amqpTemplate.convertAndSend(
				exchange.getName(),
				"quick.brown.fox",
				"test message 6");
	}
}
