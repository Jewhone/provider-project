package com.zhp.jewhone.core.constant.enums;

import com.zhp.jewhone.core.constant.RabbitmqQueueConst;

/**
 * 移动端日志Rabbit枚举类
 * @author ZHP.
 * @time 2018年4月10日 下午2:03:55
 *
 */
public enum RabbitmqEnum {
	MOBILE_LOG("mobile_exchange", RabbitmqQueueConst.MOBILE_LOG,"移动端日志");
	
	private String exchange;//交换机
	private String queue;//队列
	private String desc;//描述

	// 构造方法
	private RabbitmqEnum(String exchange, String queue, String desc) {
		this.exchange = exchange;
		this.queue = queue;
		this.desc = desc;
	}


	public String getExchange() {
		return exchange;
	}
	public String getRoutingKey(RabbitmqEnum rabbitmqEnum) {
		return rabbitmqEnum.getExchange()+"_"+rabbitmqEnum.getQueue()+"_key";
	}


	public String getQueue() {
		return queue;
	}


	public String getDesc() {
		return desc;
	}

}
