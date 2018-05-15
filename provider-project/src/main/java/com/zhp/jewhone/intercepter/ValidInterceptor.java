package com.zhp.jewhone.intercepter;

import com.zhp.jewhone.core.Result;
import com.zhp.jewhone.core.config.SysConfig;
import com.zhp.jewhone.core.constant.Const;
import com.zhp.jewhone.core.constant.enums.ResponseEnum;
import com.zhp.jewhone.core.util.JsonUtil;
import com.zhp.jewhone.core.util.annotation.Length;
import com.zhp.jewhone.core.util.annotation.Rule;
import com.zhp.jewhone.core.util.annotation.Validation;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 校验拦截器
 */
@Slf4j
public class ValidInterceptor implements MethodInterceptor {
	private SysConfig conf = SysConfig.getInstance();

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Map<String, Object> params = null;
		Method method = invocation.getMethod();
		if (method.isAnnotationPresent(Validation.class)) {
			Validation valid = method.getAnnotation(Validation.class);
			Object[] args = invocation.getArguments();
			for (Object obj : args) {
				if (obj instanceof Map) {
					params = (Map<String, Object>) obj;
				} else {
					params = JsonUtil.toMap(JsonUtil.toJson(obj));
				}
			}
			Object[] result = valid(valid, params);
			if (!(boolean) result[0]) {
				log.error(method.getName()+":"+result[1].toString());
				return new Result(ResponseEnum.ERROR.getValue(), Const.ERROR_MSG, "{}");
			}
		}
		return invocation.proceed();
	}

	private Object[] valid(Validation valid, Map<String, Object> params) {
		Object[] result;
		result = notNull(valid.notNull(), params);
		if ((boolean) result[0]) {
			result = length(valid.length(), params);
			if ((boolean) result[0]) {
				result = email(valid.email(), params);
				if ((boolean) result[0]) {
					result = rules(valid.rules(), params);
					if ((boolean) result[0]) {
						result = mobile(valid.mobile(), params);
						if ((boolean) result[0]) {
							result = number(valid.number(), params);
						}
					}
				}
			}
		}
		return result;
	}

	private Object[] number(String[] numbers, Map<String, Object> params) {
		Object[] result = new Object[] { true, ""};
		if (numbers != null && numbers.length > 0) {
			for (String filed : numbers) {
				Object param = params.get(filed);
				if (param != null && !param.toString().matches(conf.getValueByKey("number"))) {
					result[0] = false;
					result[1] = conf.getValueByKey("validator.constraints.Number.message", filed);
					break;
				}
			}
		}
		return result;
	}

	private Object[] email(String[] emails, Map<String, Object> params) {
		Object[] result = new Object[] { true, ""};
		if (emails != null && emails.length > 0) {
			for (String filed : emails) {
				Object param = params.get(filed);
				if (param == null || !param.toString().matches(conf.getValueByKey("email"))) {
					result[0] = false;
					result[1] = conf.getValueByKey("validator.constraints.Email.message", filed);
					break;
				}
			}
		}
		return result;
	}

	private Object[] mobile(String[] mobile, Map<String, Object> params) {
		Object[] result = new Object[] { true, ""};
		if (mobile != null && mobile.length > 0) {
			for (String filed : mobile) {
				Object param = params.get(filed);
				if (param == null || !param.toString().matches(conf.getValueByKey("mobile"))) {
					result[0] = false;
					result[1] = conf.getValueByKey("validator.constraints.Mobile.message", filed);
					break;
				}
			}
		}
		return result;
	}

	private Object[] notNull(String[] notNull, Map<String, Object> params) {
		Object[] result = new Object[] { true, ""};
		if (notNull != null && notNull.length > 0) {
			for (String filed : notNull) {
				Object param = params.get(filed);
				if (param == null || param.toString().matches(conf.getValueByKey("isNull"))) {
					result[0] = false;
					result[1] = conf.getValueByKey("validator.constraints.IsNull.message", filed);
					break;
				}
			}
		}
		return result;
	}

	private Object[] length(Length[] lengths, Map<String, Object> params) {
		Object[] result = new Object[] { true, ""};
		if (lengths != null && lengths.length > 0) {
			for (Length length : lengths) {
				String filed = length.filed();
				int min = length.min();
				int max = length.max();
				Object param = params.get(filed);
				if (param == null || param.toString().length() < min || param.toString().length() > max) {
					result[0] = false;
					result[1] = conf.getValueByKey("validator.constraints.Length.message", filed, min, max);
					break;
				}
			}
		}
		return result;
	}

	private Object[] rules(Rule[] rules, Map<String, Object> params) {
		Object[] result = new Object[] { true, ""};
		if (rules != null && rules.length > 0) {
			for (Rule rule : rules) {
				String filed = rule.filed();
				String regx = rule.regx();
				Object param = params.get(filed);
				if (param == null || !param.toString().matches(regx)) {
                    result[0] = false;
					result[1] = conf.getValueByKey("validator.constraints.Rule.message", filed);
				}
			}
		}
		return result;
	}
}