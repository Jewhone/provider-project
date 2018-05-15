package com.zhp.jewhone.service.rabbitmq;

public interface RabbitmqReceiveService {
	void process(String message);
}
