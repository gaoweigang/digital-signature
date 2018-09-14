package com.gwg.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class PropertiesMap {

	protected final HashMap<String, Object> properties;
	
	public PropertiesMap() {
		properties = new HashMap<String, Object>();
	}
	
	public PropertiesMap(HashMap<String, Object> properties) {
		this.properties = properties;
	}
	
	public Map<String, Object> getAll() {
		return properties;
	}
	
	public String getURLString(String key) {
		String value = (String)properties.get(key);
		if (value == null) return null;
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public String get(String key) {
		return (String)properties.get(key);
	}
	
	public String get(String key, String def) {
		String value = (String)properties.get(key);
		return value != null ? value : def;
	}
	
	public Boolean getBool(String key) {
		String value = (String)properties.get(key);
		if (value != null) {
			try {
				return Boolean.parseBoolean(value);
			} catch (Exception e) {
			}
		}
		return null;
	}

	public Integer getInt(String key) {
		String value = (String)properties.get(key);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	public Long getLong(String key) {
		String value = (String)properties.get(key);
		if (value != null) {
			try {
				return Long.parseLong(value);
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	public Float getFloat(String key) {
		String value = (String)properties.get(key);
		if (value != null) {
			try {
				return Float.parseFloat(value);
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	public Double getDouble(String key) {
		String value = (String)properties.get(key);
		if (value != null) {
			try {
				return Double.parseDouble(value);
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	public boolean getBool(String key, boolean def) {
		String value = (String)properties.get(key);
		if (value != null) {
			try {
				return Boolean.parseBoolean(value);
			} catch (Exception e) {
			}
		}
		return def;
	}

	public int getInt(String key, int def) {
		String value = (String)properties.get(key);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
			}
		}
		return def;
	}
	
	public long getLong(String key, long def) {
		String value = (String)properties.get(key);
		if (value != null) {
			try {
				return Long.parseLong(value);
			} catch (Exception e) {
			}
		}
		return def;
	}
	
	public float getFloat(String key, float def) {
		String value = (String)properties.get(key);
		if (value != null) {
			try {
				return Float.parseFloat(value);
			} catch (Exception e) {
			}
		}
		return def;
	}
	
	public double getDouble(String key, double def) {
		String value = (String)properties.get(key);
		if (value != null) {
			try {
				return Double.parseDouble(value);
			} catch (Exception e) {
			}
		}
		return def;
	}
}
