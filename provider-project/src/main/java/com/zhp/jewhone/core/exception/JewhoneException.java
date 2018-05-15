package com.zhp.jewhone.core.exception;

import com.zhp.jewhone.core.constant.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 自定义异常类(继承运行时异常)
 */
@Slf4j
public class JewhoneException extends RuntimeException implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3896794485678615761L;

	/**
	 * 消息枚举类
	 */
	private ResponseEnum msgEnum;

	public JewhoneException(ResponseEnum msgEnum) {
		super(msgEnum.getDesc());
		this.msgEnum = msgEnum;
		log.error(msgEnum.getDesc());
	}
	public JewhoneException(String msg) {
		super(msg);
		log.error(msg);
	}
	public JewhoneException(ResponseEnum msgEnum, Throwable cause) {
		super(msgEnum.getDesc(), cause);
		this.msgEnum = msgEnum;
		log.error(msgEnum.getDesc(), cause);
	}
	public JewhoneException(String msg, Throwable cause) {
		super(msg, cause);
		log.error(msg, cause);
	}
	
	/**
	 * @return the msgEnum
	 */
	public ResponseEnum getMsgEnum() {
		return msgEnum;
	}
	/**
	 * @param msgEnum the msgEnum to set
	 */
	public void setMsgEnum(ResponseEnum msgEnum) {
		this.msgEnum = msgEnum;
	}

	public static void main(String[] args) {
	}
}
