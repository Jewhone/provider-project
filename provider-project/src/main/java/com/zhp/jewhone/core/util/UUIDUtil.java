package com.zhp.jewhone.core.util;

import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * UUID随机数
 */
public class UUIDUtil {
	/**
	 * @return UUID随机数
	 */
	public static String getUUID(){
		return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
	}
	public static void main(String[] args) {
		System.out.println(getUUID());
		//09b670514e2f47338c7fc9f8b7b8ba96
		System.out.println(MD5Util.getInstance().getMD5Code("09b670514e2f47338c7fc9f8b7b8ba96"));
	}
}
