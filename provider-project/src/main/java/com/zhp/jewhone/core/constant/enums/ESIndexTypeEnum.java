package com.zhp.jewhone.core.constant.enums;

/**
 * 
 * @author ZHP.
 * @time 2018年1月31日 上午10:38:10
 *
 */
public enum ESIndexTypeEnum {
	DIARY(1, "文章"), TALK(2, "说说"), SECRET(3, "秘密"), PLAZA(4, "供求"), SYS_OPERATION_LOG(5, "API日志类");

	/**
	 * 值
	 */
	private Integer value;
	/**
	 * 描述
	 */
	private String desc;

	// 构造方法
	private ESIndexTypeEnum(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public Integer getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * 获取枚举
	 * 
	 * @param value
	 * @return
	 */
	public static ESIndexTypeEnum getByValue(int value) {
		for (ESIndexTypeEnum e : values()) {
			if (e.getValue() == value) {
				return e;
			}
		}
		return null;
	}
}
