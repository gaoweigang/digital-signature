package com.gwg.utils;

import java.util.HashMap;
import java.util.Map;

public class CookieUtils {
	public static Map<String, String> getCookie(String cookie) {
		if (cookie == null || cookie.trim().length() == 0) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String[] str = cookie.split(";");
		for (String s : str) {
			String[] v = s.split("=");
			if (v.length == 2) {
				map.put(v[0], v[1]);
			}
		}
		return map;
	}
}
