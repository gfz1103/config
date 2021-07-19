package com.buit.config.rabbitmq;

import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InitMess {
	public void init(ApplicationContext applicationContext,ObjectMapper objectMapper) {
		org.springframework.amqp.rabbit.core.RabbitTemplate rabbitmqTemplate=applicationContext.getBean(org.springframework.amqp.rabbit.core.RabbitTemplate.class);
		rabbitmqTemplate.setMessageConverter(new org.springframework.amqp.support.converter.Jackson2JsonMessageConverter(objectMapper));
	}
}
