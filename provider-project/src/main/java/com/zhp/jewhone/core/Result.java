package com.zhp.jewhone.core;

import com.zhp.jewhone.core.constant.enums.ResponseEnum;

import java.io.Serializable;

/**
 * 返回实体
 */
public class Result implements Serializable {
	private static final long serialVersionUID = 1853785175677090662L;

	private int code = ResponseEnum.SUCCESS.getValue();
	private String message = ResponseEnum.SUCCESS.getDesc();
	private Object data;
	public Result() {}
	
	/**
	 * @param code
	 * @param message
	 * @param data
	 */
	public Result(int code, String message, Object data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}
	/**
	 * 生成一个成功的实体
	 * @return
	 */
	public static  Result makeSuccess() {
		return new Result(ResponseEnum.SUCCESS);
	}
	/**
	 * 生成一个失败的实体
	 * @return
	 */
	public static Result makeError() {
		return new Result(ResponseEnum.ERROR);
	}
	public static  Result make(Boolean isSuccess) {
		if (isSuccess) {
			return new Result(ResponseEnum.SUCCESS);
		}
		return new Result(ResponseEnum.ERROR);
	}
	public static  Result make(Boolean isSuccess, String msg) {
		if (isSuccess) {
			return new Result(ResponseEnum.SUCCESS);
		}
		return new Result(ResponseEnum.ERROR);
	}
	public Result(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public Result(ResponseEnum responseEnum) {
		super();
		this.code = responseEnum.getValue();
		this.message = responseEnum.getDesc();
	}
	public Result(ResponseEnum responseEnum, String msg) {
		super();
		this.code = responseEnum.getValue();
		this.message = msg;
	}
	public Result(ResponseEnum responseEnum, Object data) {
		super();
		this.code = responseEnum.getValue();
		this.message = responseEnum.getDesc();
		this.data = data;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
}
