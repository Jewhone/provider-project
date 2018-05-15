package com.zhp.jewhone.core.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义校验
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Validation {
	/**
	 * <font color="red">非空验证</font>
	 * 
	 * @return String[]
	 * @since 1.0
	 */
	public String[] notNull() default {};

	/**
	 * <font color="red">邮箱验证</font>
	 * 
	 * @return String[]
	 * @since 1.0
	 */
	public String[] email() default {};

	/**
	 * <font color="red">手机验证</font>
	 * 
	 * @return String[]
	 * @since 1.0
	 */
	public String[] mobile() default {};

	/**
	 * <font color="red">自定义验证</font>
	 * 
	 * @return {@linkplain Rule}
	 * @since 1.0
	 */
	public Rule[] rules() default {};

	/**
	 * <font color="red">长度验证</font>
	 * 
	 * @return {@linkplain Length}
	 * @since 1.0
	 */
	public Length[] length() default {};

	/**
	 * <font color="red">数字验证</font>
	 * 
	 * @return String[]
	 * @since 1.0
	 */
	public String[] number() default {};

	/**
	 * <font color="green">标识返回页面</font>
	 * 
	 * @return {@link String}
	 * @since 1.0
	 */

	public String view() default "error";
}