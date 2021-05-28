package com.hoya.core.utils;

import java.util.regex.Pattern;

public class PhoneUtil {

	final private static String REGEX = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
	final private static Pattern PATTERN = Pattern.compile(REGEX);

	/**
	 * 校验手机号
	 * 
	 * @param phone 手机号码
	 * @return 是否正确的手机号码
	 */
	public static boolean checkPhone(String phone) {
		if (phone == null || phone.length() != 11) {
			return false;
		}

		return PATTERN.matcher(phone).matches();
	}
}
