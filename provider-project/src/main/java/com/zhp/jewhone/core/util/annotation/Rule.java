package com.zhp.jewhone.core.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义规则
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface Rule {
	/**
	 * <font color="red">校验字段</font>
	 * 
	 * @Title filed
	 * @return String
	 * @since 1.0
	 */
	public String filed();

	/**
	 * <font color="red">校验正则表达式</font>
	 * 
	 * @Title regx
	 * @return String
	 * @since 1.0
	 */
	public String regx();
}
