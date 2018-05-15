package com.zhp.jewhone.core.elasticsearch;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
@Order(1) //请注意：这里order一定要小于tx:annotation-driven的order，即先执行DataSourceAspect切面，再执行事务切面，才能获取到最终的数据源
//@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ESClientAspect {
    static Logger logger = LoggerFactory.getLogger(ESClientAspect.class);
    @Resource
	private ESClient esClient;
    /**
     * 切入点 service包及子孙包下的所有类
     */
    @Pointcut("execution(* com.zhp.jewhone.dao..*.*(..))")
    public void aspect() {
    }

    /**
     * 配置前置通知,使用在方法aspect()上注册的切入点
     */
    @Before("aspect()")
    public void before(JoinPoint point) {
        ESClientHandle.setDataSource(esClient);
    }

    @After("aspect()")
    public void after(JoinPoint point) {
        ESClientHandle.clearDataSource();
    }
}
