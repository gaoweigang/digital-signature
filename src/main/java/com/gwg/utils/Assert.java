package com.gwg.utils;

import java.util.regex.Pattern;

public final class Assert {
	private static final String COUNT_RECORD_PATTERN = "(?!0)\\d{1,8}";
	private static final String BANK_CARD_TYPE_PATTERN = "0|1|2|[\\s]{0}";
	private static final String BANK_TYPE_PATTERN = "[0-9]{7}";
	private static final String CUST_CERTIFICATION_TYPE = "[0-9A-Z]{1}|[\\s]{0}";
	private static final String AMOUNT_PATTERN = "(?!0)\\d{1,15}";
	private static final String BANK_ACCT_TYPE = "1|2|[\\s]{0}";
	private static final String CARD_VALID_DATE = "([0][1-9]|[1][0-2])[0-9]{2}|[\\s]{0}";
	private static final String CARD_CVV2 = "[0-9]{3}|[\\s]{0}";
	private static final String BANK_ACCOUNT_NO = "[0-9-]{8,32}";
	private static final String ONE_OR_TWO_REQUIRED = "[1,2]{1}";
	private static final String CURRENCY_REGEXP = "([CNY]|[USD]|[EUR]|[HKD]){3}";
	private static final String LETTER_DIGIT_CHINESE_MATCHES = "^[a-z0-9A-Z\u4e00-\u9fa5_]+$";
	private static final String CHINESE_MATCHES = "^[\u4e00-\u9fa5]+$";
	private static final String NUMBER_MATCHES = "^-?\\d+$";
	private static final String ENGLISH_MATCHES = "^[a-zA-Z]+$";
	private static final String PHONE_NO = "^1(3|4|5|7|8)\\d{9}$";

	public static void isTrue(boolean expression, String message) {
		if (!expression)
			throw new IllegalArgumentException(message);
	}

	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	public static void isNull(Object object, String message) {
		if (object != null)
			throw new IllegalArgumentException(message);
	}

	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	public static void notNull(Object object, String message) {
		if (object == null)
			throw new IllegalArgumentException(message);
	}

	public static void notNull(Object object) {
		notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	public static void hasLength(String text, int length, String message) {
		if (!hasLengthInner(text, length))
			throw new IllegalArgumentException(message);
	}

	public static void hasLength(String text) {
		hasLength(text, 1, "[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	public static void hasLength(String text, int length) {
		hasLength(text, length, "[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	public static void hasText(String text, String message) {
		if (!hasTextInnger(text))
			throw new IllegalArgumentException(message);
	}

	public static void hasText(String text) {
		hasText(text, "[Assertion failed] - this String argument[" + text + "] must have text; it must not be null, empty, or blank");
	}

	public static void noNullElements(Object[] array, String message) {
		if (array != null)
			for (int i = 0; i < array.length; i++)
				if (array[i] == null)
					throw new IllegalArgumentException(message);
	}

	public static void noNullElements(Object[] array) {
		noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
	}

	public static void isInstanceOf(Class<?> clazz, Object obj) {
		isInstanceOf(clazz, obj, "");
	}

	public static void isInstanceOf(Class<?> type, Object obj, String message) {
		notNull(type, "Type to check against must not be null");
		if (!type.isInstance(obj))
			throw new IllegalArgumentException(message + "Object of class [" + (obj != null ? obj.getClass().getName() : "null")
					+ "] must be an instance of " + type);
	}

	public static void isAssignable(Class<?> superType, Class<?> subType) {
		isAssignable(superType, subType, "");
	}

	public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
		notNull(superType, "Type to check against must not be null");
		if ((subType == null) || (!superType.isAssignableFrom(subType)))
			throw new IllegalArgumentException(message + subType + " is not assignable to " + superType);
	}

	public static void state(boolean expression, String message) {
		if (!expression)
			throw new IllegalStateException(message);
	}

	public static void state(boolean expression) {
		state(expression, "[Assertion failed] - this state invariant must be true");
	}

	private static boolean hasTextInnger(CharSequence str) {
		if (!hasLengthInner(str, 1)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	private static boolean hasLengthInner(CharSequence str, int length) {
		return (str != null) && (str.length() >= length);
	}

	public static void hasMaxLength(String str, int maxLength, String message) {
		if ((str == null) || ("".equals(str.trim())) || (!hasMaxLength(str, maxLength)))
			throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 不能为空,且长度不能超过" + maxLength);
	}

	public static boolean hasMaxLength(CharSequence str, int maxLength) {
		return (str != null) && (str.length() > 0) && (str.length() <= maxLength);
	}

	public static void isMatch(String str, String regex, String message) {
		Pattern pattern = Pattern.compile(regex);
		if (str == null || !pattern.matcher(str).matches()) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isMatchCountRecord(String str, String message) {
		Pattern pattern = Pattern.compile(COUNT_RECORD_PATTERN);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 格式不正确或者范围正确,范围为:[1-99999999]");
	}

	public static void isMatchBankCardType(String str, String message) {
		Pattern pattern = Pattern.compile(BANK_CARD_TYPE_PATTERN);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 只能为0、1或者2");
	}

	public static void isMatchBankType(String str, String message) {
		Pattern pattern = Pattern.compile(BANK_TYPE_PATTERN);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}[Assertion failed] -只能为7位数字");
	}

	public static void isMatchCustCertificationType(String str, String message) {
		Pattern pattern = Pattern.compile(CUST_CERTIFICATION_TYPE);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 只能是[0-9]或者[A-Z]");
	}

	public static void isMatchAmount(String str, String message) {
		Pattern pattern = Pattern.compile(AMOUNT_PATTERN);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 格式不正确或者范围不正确,范围为:[1-999999999999999]分");
	}

	public static void isMatchBankAcctType(String str, String message) {
		Pattern pattern = Pattern.compile(BANK_ACCT_TYPE);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 只能是1或者2");
	}

	public static void isMatchCardValidDate(String str, String message) {
		Pattern pattern = Pattern.compile(CARD_VALID_DATE);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 信用卡有效期格式错误,格式为[月月年年]");
	}

	public static void isMatchCardCvv2(String str, String message) {
		Pattern pattern = Pattern.compile(CARD_CVV2);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 信用卡CVV2码格式不正确");
	}

	public static void isMatchBankAccountNo(String str, String message) {
		Pattern pattern = Pattern.compile(BANK_ACCOUNT_NO);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 银行卡号必须为8-32位的数字或者”-“组成");
	}

	public static void isMatchStatus(String str, String message) {
		Pattern pattern = Pattern.compile(ONE_OR_TWO_REQUIRED);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 交易状态只能为1或者2");
	}

	public static void isMatchCurrency(String str, String message) {
		Pattern pattern = Pattern.compile(CURRENCY_REGEXP);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 币种只支持[CNY,USD,EUR,HKD]");
	}

	public static void isLetterDigitOrChinese(String str, String message) {
		Pattern pattern = Pattern.compile(LETTER_DIGIT_CHINESE_MATCHES);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "只能为字母,数字,汉字和下划线");
	}

	/**
	 * 是否纯汉字
	 * 
	 * @param str
	 * @return
	 */
	public static void isChinese(String str, String message) {
		Pattern pattern = Pattern.compile(CHINESE_MATCHES);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}只能为汉字");
	}

	/**
	 * 是否纯数字
	 * 
	 * @param str
	 * @return
	 */
	public static void isNumber(String str, String message) {
		Pattern pattern = Pattern.compile(NUMBER_MATCHES);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}只能为数字");
	}

	/**
	 * 是否纯字母
	 * 
	 * @param str
	 * @return
	 */
	public static void isEnglish(String str, String message) {
		Pattern pattern = Pattern.compile(ENGLISH_MATCHES);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}只能为英文字母");
	}

	public static void isMatchIdCard(String str, String message) {
		if (str == null || (str.length() != 15 && str.length() != 18))
			throw new IllegalArgumentException("{" + message + "}身份证格式不正确");
	}

	public static void isMatchPhoneNo(String str, String message) {
		Pattern pattern = Pattern.compile(PHONE_NO);
		if (str == null || !pattern.matcher(str).matches())
			throw new IllegalArgumentException("{" + message + "}手机号码格式不正确");
	}
}