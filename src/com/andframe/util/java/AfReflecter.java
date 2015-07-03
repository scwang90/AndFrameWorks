package com.andframe.util.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
/**
 * java 反射工具类
 * @author 树朾
 */
public class AfReflecter {

	/**
	 *  获取 subobj 相对 父类 supclass 的 第index个泛型参数
	 * @param subobj 对象 一般用this 如 class Type<E> 可传入 Type.this 
	 * @param supclass 父类(模板类) 如 class Type<E> 可传入 Type.class 
	 * @param index 要获取参数的序列 一般用0
	 * @return null 查找失败 否则返回参数类型Class<?>
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getActualTypeArgument(Object subobj,Class<?> supclass,int index){
		Class<?> subclass = subobj.getClass();
		List<ParameterizedType> ptypes = new ArrayList<ParameterizedType>();
		ParameterizedType ptype = null;
		while (!supclass.equals(subclass)) {
			Type type = subclass.getGenericSuperclass();
			if (type == null) {
				throw new Error("GenericSuperclass not find");
			}else if (type instanceof Class) {
				subclass = (Class<?>)type;
			}else if (type instanceof ParameterizedType) {
				ptype = ParameterizedType.class.cast(type);
				ptypes.add(ptype);
				subclass = (Class<?>)ptype.getRawType();
			}else if (type instanceof GenericArrayType) {
				GenericArrayType gtype = GenericArrayType.class.cast(type);
				subclass = (Class<?>)gtype.getGenericComponentType();
			}else{
				throw new Error("GenericSuperclass not case");
			}
		}
		Type type = null;
		do{
			type = ptypes.get(ptypes.size()-1).getActualTypeArguments()[index];
			ptypes.remove(ptypes.size()-1);
		}while(!(type instanceof Class) && ptypes.size() > 0);
		return (Class<T>)type;
	}

	/**
	 * 在clazz中获取所有Field(包括父类)
	 * @param clazz
	 * @param annot
	 * @return Field[]
	 */
	public static Field[] getField(Class<?> clazz) {
		// TODO Auto-generated method stub
		List<Field> fields = new ArrayList<Field>();
		while (!clazz.equals(Object.class)) {
			for (Field field : clazz.getDeclaredFields()) {
				fields.add(field);
			}
			clazz = clazz.getSuperclass();
		}
		return fields.toArray(new Field[0]);
	}
	
	/**
	 * 在clazz中获取所有标记annot的Field(包括父类)
	 * @param clazz
	 * @param annot
	 * @return Field[]
	 */
	public static Field[] getFieldAnnotation(Class<?> clazz, Class<? extends  Annotation> annot) {
		// TODO Auto-generated method stub
		List<Field> fields = new ArrayList<Field>();
		while (!clazz.equals(Object.class)) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(annot)) {
					fields.add(field);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return fields.toArray(new Field[0]);
	}

	/**
	 * 利用反射设置 获取type的method的Annotation(包括父类)
	 * @param type
	 * @param method
	 * @param annot
	 * @return method or null
	 */
	public static <T extends  Annotation> T getMethodAnnotation(Class<?> type, String method,  Class<T> annot) {
		// TODO Auto-generated method stub
		while (!type.equals(Object.class)) {
			for (Method dmethod : type.getDeclaredMethods()) {
				if (dmethod.getName().equals(method)) {
					if (dmethod.isAnnotationPresent(annot)) {
						return dmethod.getAnnotation(annot);
					}
				}
			}
			type = type.getSuperclass();
		}
		return null;
	}	
	/**
	 * 利用反射设置 获取type的method(包括父类)
	 * @param type
	 * @param method
	 * @return method or null
	 */
	public static Method getMethod(Class<?> type, String method) {
		// TODO Auto-generated method stub
		while (!type.equals(Object.class)) {
			for (Method dmethod : type.getDeclaredMethods()) {
				if (dmethod.getName().equals(method)) {
					return dmethod;
				}
			}
			type = type.getSuperclass();
		}
		return null;
	}	
	/**
	 * 利用反射设置 获取type的field(包括父类)
	 * @param type
	 * @param field 不支持‘.’路径
	 * @return field or null
	 */
	public static Field getField(Class<?> type, String field) {
		// TODO Auto-generated method stub
		while (!type.equals(Object.class)) {
			try {
				return type.getDeclaredField(field);
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			type = type.getSuperclass();
		}
		return null;
	}
	
	/**
	 * 利用反射设置 深层获取类型type的属性path的Field(包括父类)
	 * @param obj
	 * @param type
	 * @param path
	 * @param index
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws Exception 数组越界
	 */
	private static Field getField(Class<?> type, String[] path,int index) throws Exception{
		Field field = getField(type,path[index]);
		if (path.length == index + 1) {
			return field;
		} else if (path.length > 0) {
			return getField(field.getType(), path, index + 1);
		}
		return field;
	}

	/**
	 * 获取字段 Field(包括父类)
	 * @param model 
	 * @param field 支持‘.’路径 如 person.name
	 * @return
	 * @throws Exception
	 */
	public static Field getField(Object model, String field) throws Exception {
		// TODO Auto-generated method stub
		return getField(model.getClass(), field.split("\\."), 0);
	}

	/**
	 * 获取字段 Field(包括父类)
	 * @param model
	 * @param field 支持‘.’路径 如 person.name
	 * @return
	 */
	public static Field getFieldNoException(Object model, String field) {
		// TODO Auto-generated method stub
		try {
			return getField(model.getClass(), field.split("\\."), 0);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
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
	private static void invokeMember(Class<?> type, String[] path, Object obj,
			Object value, int index) throws Exception {
		Field field = getField(type,path[index]);
		if (path.length == index + 1) {
			field.setAccessible(true);
			field.set(obj, value);
		} else if (path.length > 0) {
			value = field.get(obj);
			invokeMember(obj.getClass(), path, obj, value, index + 1);
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
	private static Object invokeMember(Class<?> type, String[] path,Object obj, 
			int index) throws Exception  {
		Field field = getField(type,path[index]);
		field.setAccessible(true);
		Object value = field.get(obj);
		if (path.length == index + 1) {
			return value;
		} else if (path.length > 0 && value != null) {
			return invokeMember(value.getClass(), path, value, index + 1);
		}
		return value;
	}

	/**
	 * 获取 obj 属性 field 的值
	 * @param obj
	 * @param field 支持‘.’路径 如 person.name
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	public static void setMember(Object obj,String field,Object value) throws Exception {
		invokeMember(obj.getClass(), field.split("\\."), obj,value, 0);
	}

	/**
	 * 获取 obj 属性 field 的值
	 * @param obj
	 * @param field 支持‘.’路径 如 person.name
	 * @return
	 */
	public static boolean setMemberNoException(Object obj,String field,Object value) {
		try {
			invokeMember(obj.getClass(), field.split("\\."), obj,value, 0);
			return true;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			return false;
		}
	}


	/**
	 * 获取 obj 属性 field 的值
	 * @param obj
	 * @param field 支持‘.’路径 如 person.name
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	public static Object getMember(Object obj,String field) throws Exception {
		return invokeMember(obj.getClass(), field.split("\\."), obj, 0);
	}

	/**
	 * 获取 obj 属性 field 的值
	 * @param obj
	 * @param field 支持‘.’路径 如 person.name
	 * @return
	 */
	public static Object getMemberNoException(Object obj,String field) {
		try {
			return invokeMember(obj.getClass(), field.split("\\."), obj, 0);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	/**
	 * 获取 obj 属性 field 的值
	 * @param obj
	 * @param field 支持‘.’路径 如 person.name
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 数组越界
	 */
	public static <T> T getMember(Object obj,String field,Class<T> clazz) throws Exception {
		obj = invokeMember(obj.getClass(), field.split("\\."), obj, 0);
		if(clazz.isInstance(obj)){
			clazz.cast(obj);
		}
		return null;
	}

	/**
	 * 获取 obj 属性 field 的值
	 * @param obj
	 * @param field 支持‘.’路径 如 person.name
	 * @return
	 */
	public static <T> T  getMemberNoException(Object obj,String field,Class<T> clazz) {
		try {
			return clazz.cast(invokeMember(obj.getClass(), field.split("\\."), obj, 0));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			return null;
		}
	}


}
