package com.zhp.jewhone.core.exception;

import com.zhp.jewhone.core.Result;
import com.zhp.jewhone.core.constant.enums.ResponseEnum;
import com.zhp.jewhone.core.util.SendMailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理.
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandle {
	@ExceptionHandler(value=Exception.class)
	@ResponseBody
	public Result handle(Exception e, HttpServletRequest request) {
		ResponseEnum msgEnum = ResponseEnum.ERROR;
		if (e instanceof JewhoneException) {
			JewhoneException exception = (JewhoneException)e;
			return new Result(exception.getMsgEnum());
		}
		log.error(msgEnum.getDesc(),e);
		SendMailUtil.send(e.toString());
		return new Result(ResponseEnum.ERROR,"不在服务区");
	}
}
