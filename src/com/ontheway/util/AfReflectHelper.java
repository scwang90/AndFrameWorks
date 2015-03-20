package com.ontheway.util;

import java.lang.reflect.Field;
/**
 * @Description: java反射工具类
 * @Author: scwang
 * @Version: V1.0, 2015-3-13 下午4:58:01
 * @Modified: 初次创建AfReflectHelper类
 */
public class AfReflectHelper {

	/**
	 * 利用反射设置 对象obj的属性path为 value
	 * @param type
	 * @param path
	 * @param obj
	 * @param value
	 * @param index 必须指定为 0
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void InvokeMember(Class<?> type, String[] path, Object obj,
			Object value, int index) throws Exception {
		Field field = type.getField(path[index]);
		if (path.length == index + 1) {
			field.setAccessible(true);
			field.set(obj, value);
		} else if (path.length > 0) {
			value = field.get(obj);
			InvokeMember(obj.getClass(), path, obj, value, index + 1);
		}
	}

	/**
	 * 利用反射设置 获取对象obj的属性path的value
	 * @param obj
	 * @param type
	 * @param path
	 * @param index
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	public static Object InvokeMember(Class<?> type, String[] path,Object obj, 
			int index) throws Exception  {
		Field field = type.getField(path[index]);
		Object value = field.get(obj);
		if (path.length == index + 1) {
			return value;
		} else if (path.length > 0 && value != null) {
			return InvokeMember(value.getClass(), path, value, index + 1);
		}
		return value;
	}


	/**
	 * 利用反射设置 深层获取类型type的属性path的Field
	 * @param obj
	 * @param type
	 * @param path
	 * @param index
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws Exception 数组越界
	 */
	public static Field GetField(Class<?> type, String[] path,int index) throws Exception{
		Field field = type.getField(path[index]);
		if (path.length == index + 1) {
			return field;
		} else if (path.length > 0) {
			return GetField(field.getType(), path, index + 1);
		}
		return field;
	}
	/**
	 * 获取 obj 属性 field 的值
	 * @param obj
	 * @param field	如 "Name"
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	public static void SetMember(Object obj,String field,Object value) throws Exception {
		SetMember(obj.getClass(),obj,field,value);
	}


	/**
	 * 获取 obj 属性 field 的值
	 * @param type 指定 obj 的类型 
	 * @param obj
	 * @param field	如 "Name"
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	private static void SetMember(Class<?> type, Object obj,String field,Object value) throws Exception {
		// TODO Auto-generated method stub
		InvokeMember(type, field.split("\\."), obj,value, 0);
	}
	


	/**
	 * 获取 obj 属性 field 的值
	 * @param obj
	 * @param field	如 "Name"
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	public static boolean SetMemberNoException(Object obj,String field,Object value) {
		return SetMemberNoException(obj.getClass(),obj,field,value);
	}


	/**
	 * 获取 obj 属性 field 的值
	 * @param type 指定 obj 的类型 
	 * @param obj
	 * @param field	如 "Name"
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	public static boolean SetMemberNoException(Class<?> type, Object obj,String field,Object value){
		// TODO Auto-generated method stub
		try {
			InvokeMember(type, field.split("\\."), obj,value, 0);
			return true;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	

	/**
	 * 获取 obj 属性 field 的值
	 * @param obj
	 * @param field	如 "Name"
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	public static Object GetMember(Object obj,String field) throws Exception {
		return GetMember(obj.getClass(),obj,field);
	}


	/**
	 * 获取 obj 属性 field 的值
	 * @param type 指定 obj 的类型 
	 * @param obj
	 * @param field	如 "Name"
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	private static Object GetMember(Class<?> type, Object obj,String field) throws Exception {
		// TODO Auto-generated method stub
		return InvokeMember(type, field.split("\\."), obj, 0);
	}
	


	/**
	 * 获取 obj 属性 field 的值
	 * @param obj
	 * @param field	如 "Name"
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	public static Object GetMemberNoException(Object obj,String field) {
		return GetMemberNoException(obj.getClass(),obj,field);
	}


	/**
	 * 获取 obj 属性 field 的值
	 * @param type 指定 obj 的类型 
	 * @param obj
	 * @param field	如 "Name"
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	public static Object GetMemberNoException(Class<?> type, Object obj,String field){
		// TODO Auto-generated method stub
		try {
			return InvokeMember(type, field.split("\\."), obj, 0);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	

	/**
	 * 获取 obj 属性 field 的值
	 * @param obj
	 * @param field	如 "Name"
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	public static <T> T GetMember(Object obj,String field,Class<T> clazz) throws Exception {
		return GetMember(obj.getClass(),obj,field,clazz);
	}


	/**
	 * 获取 obj 属性 field 的值
	 * @param type 指定 obj 的类型 
	 * @param obj
	 * @param field	如 "Name"
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	private static  <T> T  GetMember(Class<?> type, Object obj,String field,Class<T> clazz) throws Exception {
		// TODO Auto-generated method stub
		obj = InvokeMember(type, field.split("\\."), obj, 0);
		if(clazz.isInstance(obj)){
			clazz.cast(obj);
		}
		return null;
	}
	


	/**
	 * 获取 obj 属性 field 的值
	 * @param obj
	 * @param field	如 "Name"
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	public static <T> T  GetMemberNoException(Object obj,String field,Class<T> clazz) {
		return GetMemberNoException(obj.getClass(),obj,field,clazz);
	}


	/**
	 * 获取 obj 属性 field 的值
	 * @param type 指定 obj 的类型 
	 * @param obj
	 * @param field	如 "Name"
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	public static <T> T  GetMemberNoException(Class<?> type, Object obj,String field,Class<T> clazz){
		// TODO Auto-generated method stub
		try {
			return clazz.cast(InvokeMember(type, field.split("\\."), obj, 0));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	/**
	 * 
	 * @param model
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static Field GetField(Object model, String field) throws Exception {
		// TODO Auto-generated method stub
		return GetField(model.getClass(), field.split("\\."), 0);
	}

	/**
	 * 
	 * @param model
	 * @param field
	 * @return
	 */
	public static Field GetFieldNoException(Object model, String field) {
		// TODO Auto-generated method stub
		try {
			return GetField(model.getClass(), field.split("\\."), 0);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
}
