package com.zhp.jewhone.core.exception;

import com.zhp.jewhone.core.util.SendMailUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Service 异常拦截
 */
@Aspect
@Component
public class ServiceExceptionAspect {

	/**
	 * @within(org.springframework.stereotype.Service)，拦截带有 @Service 注解的类的所有方法
	 * @annotation(org.springframework.web.bind.annotation.RequestMapping)，拦截带有@RquestMapping的注解方法
	 */
	@Pointcut("@within(org.springframework.stereotype.Service) && execution(public * *(..))")
	private void servicePointcut() {
	}

	/**
	 * 拦截service层异常，记录异常日志，并设置对应的异常信息 目前只拦截Exception，是否要拦截Error需再做考虑
	 *
	 * @param e
	 *            异常对象
	 */
	@AfterThrowing(pointcut = "servicePointcut()", throwing = "e")
	public void handle(JoinPoint point, Exception e) {
		SendMailUtil.send(e.toString());
		throw new JewhoneException("服务不可用", e);
	}

}
