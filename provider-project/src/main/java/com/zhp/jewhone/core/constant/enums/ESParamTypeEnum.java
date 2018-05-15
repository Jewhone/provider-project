package com.zhp.jewhone.core.constant.enums;


/**
 * 
 * Description:  搜索参数类型枚举
 * @author zhp.
 * @time 2018年1月31日 上午10:49:34
 * @version 1.0
 */
public enum ESParamTypeEnum {
	DEFAULT(0,"全部"),
	TALK(2,"说说"),
	DIARY(3,"原创文章"),
	THIRDDIARY(4,"转载文章"),
	PLAZA(5,"供求"),
	SECRET(6,"秘密");
	/**
	 * 值
	 */
	private Integer value;
	/**
	 * 描述
	 */
	private String desc;

	// 构造方法
	private ESParamTypeEnum(Integer value, String desc) {
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
	public static ESParamTypeEnum getByValue(int value) {
		for (ESParamTypeEnum e : values()) {
			if (e.getValue() == value) {
				return e;
			}
		}
		return null;
	}
}
