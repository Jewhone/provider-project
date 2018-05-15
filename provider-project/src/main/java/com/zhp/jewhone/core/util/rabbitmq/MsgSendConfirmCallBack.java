package com.zhp.jewhone.core.util.rabbitmq;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
public class MsgSendConfirmCallBack implements RabbitTemplate.ConfirmCallback {
	
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		log.info(" 回调id:" + correlationData);
		if (ack) {
			log.info("消息成功消费");
			log.info("ack: "+ack);
		} else {
			log.info("消息消费失败:" + cause + "\n重新发送");

		}
	}
}
