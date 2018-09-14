package com.gwg.utils;

import java.util.regex.Pattern;

public class RegexUtils {
	private static final Pattern account_pattern = Pattern
			.compile("[A-Z0-9a-z._%+-]{4,32}");
	private static final Pattern email_pattern = Pattern
			.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
	private static final Pattern phone_pattern = Pattern
			.compile("1[1-9][0-9]{9}");
	private static final Pattern password_pattern = Pattern
			.compile("[A-Z0-9a-z~`!@#$%^&*()_=+{}|;:'\"<,>.?/\\\\\\[\\]-]{4,32}");

	private static final Pattern number_pattern = Pattern
			.compile("[+-]?[1-9]+[0-9]*(\\.[0-9]+)?");
	private static final Pattern english_pattern = Pattern
			.compile("^[a-zA-Z]+$");
	private static final Pattern chinese_pattern = Pattern
			.compile("^[\u4e00-\u9fa5]+$");
	private static final Pattern litter_digit_chinese_pattern = Pattern
			.compile("^[a-z0-9A-Z\u4e00-\u9fa5_]+$");

	public static boolean isAccount(String account) {
		return account_pattern.matcher(account).matches();
	}

	public static boolean isEmail(String email) {
		return email != null && email.length() < 128
				&& email_pattern.matcher(email).matches();
	}

	public static boolean isPhone(String phone) {
		return phone_pattern.matcher(phone).matches();
	}

	public static boolean isPassword(String password) {
		return password_pattern.matcher(password).matches();
	}

	/**
	 * 是否纯数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (str == null)
			return false;
		return number_pattern.matcher(str).matches();
	}

	/**
	 * 是否纯字母
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEnglish(String str) {
		if (str == null)
			return false;
		return english_pattern.matcher(str).matches();
	}

	/**
	 * 是否纯汉字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		if (str == null)
			return false;
		return chinese_pattern.matcher(str).matches();
	}

	/**
	 * 是否为字母,数字,汉字,下划线
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isLetterDigitOrChinese(String str) {
		if (str == null) {
			return false;
		}
		return litter_digit_chinese_pattern.matcher(str).matches();
	}
}
