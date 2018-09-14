package com.gwg.utils;

import com.alibaba.fastjson.JSONObject;

import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtils {

	public static final String EMPTY = "";
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	public static String firstUpcase(String text) {
		if (text != null && text.length() > 0) {
			if (text.length() == 1) {
				return text.toUpperCase();
			}
			return text.substring(0, 1).toUpperCase() + text.substring(1);
		}
		return text;
	}

	public static String firstLowerCase(String text) {
		if (text != null && text.length() > 0) {
			if (text.length() == 1) {
				return text.toLowerCase();
			}
			return text.substring(0, 1).toLowerCase() + text.substring(1);
		}
		return text;
	}

	public static String getter(String field) {
		return "get" + firstUpcase(field);
	}

	public static String setter(String field) {
		return "set" + firstUpcase(field);
	}

	public static String indent(int idx) {
		StringBuffer result = new StringBuffer(idx * 4 + 1);
		for (int i = 0; i < idx; i++) {
			result.append("    ");
		}
		return result.toString();
	}

	/**
	 * Parse1
	 */

	public static boolean booleanValueOf(String s) {
		try {
			return Boolean.parseBoolean(s);
		} catch (Exception e) {
		}
		return false;
	}

	public static int intValueOf(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
		}
		return 0;
	}

	public static long longValueOf(String s) {
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
		}
		return 0L;
	}

	public static float floatValueOf(String s) {
		try {
			return Float.parseFloat(s);
		} catch (Exception e) {
		}
		return 0f;
	}

	public static double doubleValueOf(String s) {
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
		}
		return 0d;
	}

	/**
	 * Parse2
	 */

	public static boolean booleanValueOf(String s, boolean def) {
		try {
			return Boolean.parseBoolean(s);
		} catch (Exception e) {
		}
		return def;
	}

	public static int intValueOf(String s, int def) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
		}
		return def;
	}

	public static long longValueOf(String s, long def) {
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
		}
		return def;
	}

	public static float floatValueOf(String s, float def) {
		try {
			return Float.parseFloat(s);
		} catch (Exception e) {
		}
		return def;
	}

	public static double doubleValueOf(String s, double def) {
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
		}
		return def;
	}

	public static String stringValueOf(Object value, String defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value.toString();
	}

	public static String stringValueOf(Object value) {
		if (value == null) {
			return "";
		}
		return value.toString();
	}

	public static int[] versionValueOf(String version) {
		int[] ret = { 0, 0, 0 };
		if (version != null) {
			String[] nums = version.replace('.', '-').split("-");
			for (int i = 0; i < nums.length && i < ret.length; i++) {
				ret[i] = intValueOf(nums[i], 0);
			}
		}
		return ret;
	}

	public static String toJsonString(Object o) {
		return JSONObject.toJSONString(o);
	}

	/**
	 * 字符串转list
	 * 
	 * @param str
	 * @param sym
	 * @return
	 */
	public static List<?> stringToList(String str, String sym, Function<String, ?> func) {
		List<?> list = new ArrayList<Object>();
		if (StringUtils.isBlank(str)) {
			return list;
		}
		return Stream.of(str.split(sym)).map(func).collect(Collectors.toList());
	}

	/**
	 * list转字符串
	 * 
	 * @param list
	 * @param sym
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String listToString(List<?> list, String sym) {
		if (Objects.isNull(list)) {
			return "";
		}
		Optional<Object> optional = ((List<Object>) list).stream().reduce((a, b) -> a + sym + b);
		if (optional.isPresent()) {
			return optional.get().toString();
		}
		return "";
	}

	/**
	 * 获取字符串的长度，中文占一个字符,英文数字占半个字符
	 *
	 * @param value
	 *            指定的字符串
	 * @return 字符串的长度
	 */
	public static int length(String value) {
		double valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < value.length(); i++) {
			// 获取一个字符
			String temp = value.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chinese)) {
				// 中文字符长度为2
				valueLength += 2;
			} else {
				// 其他字符长度为1
				valueLength += 1;
			}
		}
		// 进位取整
		return (int) Math.ceil(valueLength);
	}

	/**
	 * 判断字符串为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 判断字符串非空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !StringUtils.isEmpty(str);
	}

	public static String nullToEmpty(String string) {
		return (string == null) ? "" : string;
	}

	public static String emptyToNull(String string) {
		return isEmpty(string) ? null : string;
	}

	/**
	 * 去除空串
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return str == null ? null : str.trim();
	}

	public static String trimToNull(String str) {
		return isEmpty(str) ? null : str.trim();
	}

	public static String trimToEmpty(String str) {
		return str == null ? EMPTY : str.trim();
	}

	/**
	 * 判断是否全是空字符
	 *
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 下划线转java命名规范
	 * 
	 * @param str
	 * @return
	 */
	public static String getJavaName(String str) {
		StringBuilder sb = new StringBuilder();
		String[] arr = str.split("_");
		sb.append(arr[0]);
		for (int i = 1; i < arr.length; i++) {
			String temp = arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1, arr[i].length());
			sb.append(temp);
		}

		return sb.toString();
	}

	public static String random(int number) {
		Random random = new Random();
		int randomInt = random.nextInt(Double.valueOf(Math.pow(10d, number)).intValue() - Double.valueOf(Math.pow(10d, number - 1)).intValue() + 1);
		return String.valueOf(randomInt + Double.valueOf(Math.pow(10d, number - 1)).intValue());
	}

	/**
	 * 字符串拼接
	 * 
	 * @param params
	 * @return
	 */
	public static String concat(String... params) {
		StringBuilder sb = new StringBuilder();
		for (String v : params) {
			sb.append(v);
		}
		return sb.toString();
	}
}
