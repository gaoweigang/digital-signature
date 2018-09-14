package com.gwg.utils;



import org.apache.commons.lang.math.RandomUtils;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Utils {

	private static final String NUMBER_MATCHES = "[+-]?[1-9]+[0-9]*(\\.[0-9]+)?";
	private static final String ENGLISH_MATCHES = "^[a-zA-Z]+$";
	private static final String CHINESE_MATCHES = "^[\u4e00-\u9fa5]+$";
	private static final String LETTER_DIGIT_CHINESE_MATCHES = "^[a-z0-9A-Z\u4e00-\u9fa5_]+$";
	private static final String RANDOM_NUM = "0123456789";

	/**
	 * 随机生成length个数字
	 * 
	 * @param length
	 * @return 数字
	 */
	public static String randomNum(int length) {
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		int range = RANDOM_NUM.length();
		for (int i = 0; i < length; i++) {
			sb.append(RANDOM_NUM.charAt(r.nextInt(range)));
		}
		return sb.toString();
	}

	/**
	 * 判断是否为fans
	 * 
	 * @param isExist
	 * @return
	 */
	public static int isFans(boolean isExist) {
		return isExist ? 1 : 0;
	}

	/**
	 * 装化Long为long 当为null时返回0
	 * 
	 * @param value
	 * @return
	 */
	public static long longValueOf(Long value) {
		return value == null ? 0L : value;
	}

	/**
	 * 装化Integer为int 当为null时返回0
	 * 
	 * @param value
	 * @return
	 */
	public static int intValueOf(Integer value) {
		return value == null ? 0 : value;
	}

	/**
	 * 装化Integer为int 当为null时返回0
	 * 
	 * @param value
	 * @return
	 */
	public static String stringValueOf(String value) {
		return value == null ? "" : value;
	}

	/**
	 * 随机一个int数,max为是否包含最大值
	 * 
	 * @param rand
	 * @param max
	 * @return
	 */
	public static int randomInt(int rand, boolean max) {
		if (max) {
			return RandomUtils.nextInt(rand + 1);
		}
		return RandomUtils.nextInt(rand);
	}

	/**
	 * 随机一个int数,max为是否包含最大值
	 * 
	 * @param max
	 * @return
	 */
	public static int randomInt(int min, int max) {
		Random random = new Random();
		return random.nextInt(max) % (max - min + 1) + min;
	}

	/**
	 * 随机一个int数,max为是否包含最大值
	 * 
	 * @param rand
	 * @return
	 */
	public static int randomInt(int rand) {
		return RandomUtils.nextInt(rand);
	}

	/**
	 * 随机一个uuid的字符串
	 * 
	 * @return
	 */
	public static String randomUID() {
		return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}

	/**
	 * 用户检测编码是否为utf8
	 * 
	 * @return
	 */
	public static boolean checkCharset(String userName) {
		if (!Charset.forName("UTF-8").newEncoder().canEncode(userName)) {
			// 如果为空直接返回false
			return false;
		}
		// 长度限制
		return true;
	}

	/**
	 * 把一个浮点数保留5位小数
	 * 
	 * @param f
	 * @return
	 */
	public static double transFloat(double f) {
		DecimalFormat df = new DecimalFormat("#.00");
		return Double.parseDouble(df.format(f));
	}

	/**
	 * 　　 * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回 　　 * @param sourceDate 　　 * @param
	 * formatLength 　　 * @return 重组后的数据 　　
	 */
	public static String frontCompWithZore(long sourceDate, int formatLength) {
		String newString = String.format("%0" + formatLength + "d", sourceDate);
		return newString;
	}

	/**
	 * 切分list
	 * 
	 * @param targetList
	 * @param splitSize
	 * @return
	 */
	public static <E> List<List<E>> splitList(List<E> targetList, Integer splitSize) {
		if (targetList == null)
			return null;
		Integer size = targetList.size();
		int arraySize = (size % splitSize) == 0 ? size / splitSize : size / splitSize + 1;
		List<List<E>> resultList = new ArrayList<List<E>>(arraySize);
		if (size <= splitSize) {
			resultList.add(targetList);
		} else {
			for (int i = 0; i < size; i += splitSize) {
				// 用于限制最后一部分size小于splitSize的list
				Integer limit = i + splitSize;
				if (limit > size) {
					limit = size;
				}
				resultList.add(targetList.subList(i, limit));
			}
		}
		return resultList;
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
		return str.matches(NUMBER_MATCHES);
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
		return str.matches(ENGLISH_MATCHES);
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
		return str.matches(CHINESE_MATCHES);
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
		return str.matches(LETTER_DIGIT_CHINESE_MATCHES);
	}
}
