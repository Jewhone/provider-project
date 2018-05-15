package com.zhp.jewhone.service.rabbitmq.impl;

import com.zhp.jewhone.core.constant.Const;
import com.zhp.jewhone.core.constant.RabbitmqQueueConst;
import com.zhp.jewhone.service.admin.ApiSearchService;
import com.zhp.jewhone.service.rabbitmq.MobileMessageConsumer;
import com.zhp.jewhone.service.rabbitmq.RabbitmqReceiveService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RabbitListener(queues = {RabbitmqQueueConst.MOBILE_LOG})//监听器监听指定的Queue
public class RabbitmqReceiveServiceImpl  implements RabbitmqReceiveService {
	
	@Resource
	private ApiSearchService apiSearchService;

	@RabbitHandler
	@Override
    public void process(String message) {
		apiSearchService.addData(Const.ES_MOBILE_LOG, message);
		new MobileMessageConsumer();
    }
}
