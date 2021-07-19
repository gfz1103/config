package com.buit.config.rabbitmq;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 设置
 */
@Configuration
public class ChangeMessageConverter  {
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Bean // 初始化方法的注解方式 等同与init-method=init
	@ConditionalOnClass(org.springframework.amqp.rabbit.core.RabbitTemplate.class)
	public void init() {
		InitMess initMess=new InitMess();
		initMess.init(applicationContext, objectMapper);
	}
}