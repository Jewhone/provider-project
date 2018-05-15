package com.zhp.jewhone.core.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 长度
 * 
 * @author PzC.
 * @time 2017年1月6日 下午2:19:09
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface Length {
	/**
	 * <font color="red">校验字段</font>
	 * 
	 * @Title filed
	 * @return String
	 * @since 1.0
	 */
	public String filed();

	/**
	 * <font color="red">最小长度</font>
	 * 
	 * @Title min
	 * @return int
	 * @since 1.0
	 */
	public int min();

	/**
	 * <font color="red">最大长度</font>
	 * 
	 * @Title max
	 * @return int
	 * @since 1.0
	 */
	public int max();

}