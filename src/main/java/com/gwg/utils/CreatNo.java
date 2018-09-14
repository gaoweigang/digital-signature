package com.gwg.utils;

/**
 * 生成各种NO
 * 
 * @author pro
 *
 */
public class CreatNo {
	public static String creatFlowNo() {
		return StringUtils.concat(DateUtil.dateFormat(DateUtil.currentDate(), DateUtil.TIME_FORMAT_1), Utils.randomNum(10));
	}
}
