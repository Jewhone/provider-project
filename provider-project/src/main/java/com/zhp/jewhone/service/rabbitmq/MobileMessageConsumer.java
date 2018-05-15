package com.zhp.jewhone.service.rabbitmq;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

/**
 * 消费确认
 * 
 * @author ZHP.
 * @time 2018年4月12日 上午11:04:41
 *
 */
@Slf4j
public class MobileMessageConsumer implements ChannelAwareMessageListener {

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		if (message.getMessageProperties().getHeaders().get("error") != null) {
			log.error("rabbitmq接收到错误的信息：" + message.getBody().toString());
			// 手动进行应答
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			// 重新发送消息到队尾
			channel.basicPublish(message.getMessageProperties().getReceivedExchange(), message.getMessageProperties()
					.getReceivedRoutingKey(), MessageProperties.PERSISTENT_TEXT_PLAIN, JSON.toJSONBytes(new Object()));
		}else {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); // 确认消息成功消费
		}
	}
	
}
