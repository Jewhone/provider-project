package com.zhp.jewhone.core.util.rabbitmq;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
public class MsgSendReturnCallback implements RabbitTemplate.ReturnCallback {

	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		String msgJson = new String(message.getBody());
		log.info("回馈消息：" + msgJson);
	}
}
