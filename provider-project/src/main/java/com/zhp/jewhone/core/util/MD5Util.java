package com.zhp.jewhone.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密
 * @time 2017年1月6日 下午2:33:30
 * 
 */
public class MD5Util {
	private static MD5Util instance = new MD5Util();

	private MD5Util() {
	}

	public static MD5Util getInstance() {
		if (null == instance)
			instance = new MD5Util();
		return instance;
	}

	public String getMD5Code(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
		byte[] md5Bytes = md5.digest(inStr.getBytes());
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString().toUpperCase();
	}
	
	
	
	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */
	public String convertMD5(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		return new String(a);
	}

	public static void main(String[] args) {
		MD5Util util = MD5Util.getInstance();
		String str = "哈";
		System.out.println(util.getMD5Code(str));
	}
}
