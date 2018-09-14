package com.gwg.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 功能：json处理类
 */
public class JsonUtil {
	/**
	 * 功能：对象转换成json
	 */
	public static String convertObjToJson(Object object){

		
		if(object == null){
			return null;
		}else{
			ObjectMapper mapper = new ObjectMapper();
		    String json = null;
			try {
				json = mapper.writeValueAsString(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		    return json;
		}
	}
	/**
	 * 功能：json转换成对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object convertJsonToObj(String json,Class c){
		if(json == null){
			return null;
		}else{
			ObjectMapper mapper = new ObjectMapper();
			Object object = null;
			try {
				object = mapper.readValue(json, c);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return object;
		}
	}
	
	/**
	 * 功能：json转换成对象
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Object convertJsonToObj(String json,TypeReference c){
		if(json == null){
			return null;
		}else{
			ObjectMapper mapper = new ObjectMapper();
			Object object = null;
			try {
				object = mapper.readValue(json, c);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return object;
		}
	}
}