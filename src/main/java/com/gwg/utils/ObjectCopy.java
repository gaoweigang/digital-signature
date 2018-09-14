package com.gwg.utils;

import org.apache.commons.beanutils.PropertyUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 复制一个对象
 * 
 * @author houzhiwei
 *
 */
public class ObjectCopy {

	public static Object copy(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Collection<?>) {
			return copyToList((Collection<?>) obj);
		} else if (obj.getClass().isArray()) {
			return copyToList((Object[]) obj);
		} else {
			return copyToOne(obj);
		}
	}

	public static Object copy(Class<?> classType, Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Collection<?>) {
			return copyToList(classType, (Collection<?>) obj);
		} else if (obj.getClass().isArray()) {
			return copyToList(classType, (Object[]) obj);
		} else {
			return copyToOne(classType, obj);
		}
	}

	// 该方法实现对Customer对象的拷贝操作
	private static Object copyToOne(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			Class<?> classType = obj.getClass();
			Object objCopy = classType.newInstance();
			PropertyUtils.copyProperties(objCopy, obj);
			return objCopy;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Object copyToOne(Class<?> classType, Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			Object objCopy = classType.newInstance();
			PropertyUtils.copyProperties(objCopy, obj);
			return objCopy;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static Object copyToList(Collection<?> coll) {
		if (coll == null || coll.isEmpty()) {
			return coll;
		}
		try {
			List<Object> objList = new ArrayList<Object>();
			for (Object v : coll) {
				objList.add(copyToOne(v));
			}
			return objList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Object copyToList(Class<?> classType, Collection<?> coll) {
		if (coll == null || coll.isEmpty()) {
			return coll;
		}
		try {
			List<Object> objList = new ArrayList<Object>();
			for (Object v : coll) {
				objList.add(copyToOne(classType, v));
			}
			return objList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Object copyToList(Object[] array) {
		if (array == null || array.length == 0) {
			return array;
		}
		try {
			List<Object> objList = new ArrayList<Object>();
			for (Object v : array) {
				objList.add(copyToOne(v));
			}
			return objList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Object copyToList(Class<?> classType, Object[] array) {
		if (array == null || array.length == 0) {
			return array;
		}
		try {
			List<Object> objList = new ArrayList<Object>();
			for (Object v : array) {
				objList.add(copyToOne(classType, v));
			}
			return objList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void copyProperties(final Object dest, final Object orig) throws Exception {
		try {
			PropertyUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
