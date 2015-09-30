package com.andframe.util.java;

import com.google.gson.internal.UnsafeAllocator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
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
	 * 获取 subobj 相对 父类 supclass 的 第index个泛型参数
	 *
	 * @param subobj   对象 一般用this 如 class Type<E> 可传入 Type.this
	 * @param supclass 父类(模板类) 如 class Type<E> 可传入 Type.class
	 * @param index    要获取参数的序列 一般用0
	 * @return null 查找失败 否则返回参数类型Class<?>
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getActualTypeArgument(Object subobj, Class<?> supclass, int index) {
		Class<?> subclass = subobj.getClass();
		List<ParameterizedType> ptypes = new ArrayList<ParameterizedType>();
		ParameterizedType ptype = null;
		while (!supclass.equals(subclass)) {
			Type type = subclass.getGenericSuperclass();
			if (type == null) {
				throw new Error("GenericSuperclass not find");
			} else if (type instanceof Class) {
				subclass = (Class<?>) type;
			} else if (type instanceof ParameterizedType) {
				ptype = ParameterizedType.class.cast(type);
				ptypes.add(ptype);
				subclass = (Class<?>) ptype.getRawType();
			} else if (type instanceof GenericArrayType) {
				GenericArrayType gtype = GenericArrayType.class.cast(type);
				subclass = (Class<?>) gtype.getGenericComponentType();
			} else {
				throw new Error("GenericSuperclass not case");
			}
		}
		Type type = null;
		do {
			type = ptypes.get(ptypes.size() - 1).getActualTypeArguments()[index];
			ptypes.remove(ptypes.size() - 1);
		} while (!(type instanceof Class) && ptypes.size() > 0);
		return (Class<T>) type;
	}

	/**
	 * 在clazz中获取所有Field(包括父类)
	 *
	 * @param clazz 开始扫描类型
	 * @return Field[]
	 */
	public static Field[] getField(Class<?> clazz) {
		return getField(clazz, Object.class);
	}

	/**
	 * 在clazz中获取所有Field(向父类扫描知道stop停止)
	 *
	 * @param clazz    开始扫描类型
	 * @param stoptype 停止扫描类型 stop 本身不被扫描 必须是 clazz 父类
	 * @return Field[]
	 */
	public static Field[] getField(Class<?> clazz, Class<?> stoptype) {
		List<Field> fields = new ArrayList<Field>();
		while (!clazz.equals(stoptype)) {
			for (Field field : clazz.getDeclaredFields()) {
				fields.add(field);
			}
			clazz = clazz.getSuperclass();
		}
		return fields.toArray(new Field[0]);
	}

	/**
	 * 在clazz中获取所有标记annot的Field(包括父类)
	 *
	 * @param clazz 开始扫描类型
	 * @param annot Annotation
	 * @return Field[]
	 */
	public static Field[] getFieldAnnotation(Class<?> clazz, Class<? extends Annotation> annot) {
		return getFieldAnnotation(clazz, Object.class, annot);
	}

	/**
	 * 在clazz中获取所有标记annot的Field(包括父类)
	 *
	 * @param type     开始扫描类型
	 * @param stoptype 停止扫描类型
	 * @param annot    Annotation
	 * @return Field[]
	 */
	public static Field[] getFieldAnnotation(Class<?> type, Class<?> stoptype, Class<? extends Annotation> annot) {
		Class<?> clazz = type;
		List<Field> fields = new ArrayList<Field>();
		while (!clazz.equals(stoptype)) {
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
	 * 在clazz中获取所有标记 annot 的 Method (包括父类)
	 *
	 * @param clazz 开始扫描类型
	 * @param annot Annotation
	 * @return Field[]
	 */
	public static Method[] getMethodAnnotation(Class<?> clazz, Class<? extends Annotation> annot) {
		return getMethodAnnotation(clazz, Object.class, annot);
	}

	/**
	 * 在clazz中获取所有标记 annot 的 Method (包括父类)
	 *
	 * @param type     开始扫描类型
	 * @param stoptype 停止扫描类型
	 * @param annot    Annotation
	 * @return Field[]
	 */
	public static Method[] getMethodAnnotation(Class<?> type, Class<?> stoptype, Class<? extends Annotation> annot) {
		List<Method> methods = new ArrayList<Method>();
		while (!type.equals(stoptype)) {
			for (Method method : type.getDeclaredMethods()) {
				if (method.isAnnotationPresent(annot)) {
					methods.add(method);
				}
			}
			type = type.getSuperclass();
		}
		return methods.toArray(new Method[0]);
	}

	/**
	 * 获取 type 的 method 的 Annotation (包括父类)
	 *
	 * @param type   对象类型
	 * @param method 方法名称
	 * @param annot  Annotation
	 * @return method or null
	 */
	public static <T extends Annotation> T getMethodAnnotation(Class<?> type, String method, Class<T> annot) {
		return getMethodAnnotation(type, Object.class, method, annot);
	}

	/**
	 * 获取 type 的 method 的 Annotation (包括父类)
	 *
	 * @param type   对象类型
	 * @param method 方法名称
	 * @param annot  Annotation
	 * @return method or null
	 */
	public static <T extends Annotation> T getMethodAnnotation(Class<?> type, Class<?> stoptype, String method, Class<T> annot) {
		while (!type.equals(stoptype)) {
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
	 * 获取type的method(包括父类)
	 *
	 * @param type   对象类型
	 * @param method 方法名称
	 * @return method or null
	 */
	public static Method getMethod(Class<?> type, String method) {
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
	 * 获取type的method(包括父类)
	 *
	 * @param type   对象类型
	 * @param method 方法名称
	 * @param args   如果 args 没有null 可以精确查找方法
	 * @return method or null
	 */
	public static Method getMethod(Class<?> type, String method, Object... args) {
		if (args != null) {
			Class<?>[] parameterTypes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				if (args[i] != null) {
					parameterTypes[i] = args[i].getClass();
				} else {
					parameterTypes = null;
					break;
				}
			}
			if (parameterTypes != null) {
				Method method1 = getMethod(type, method, parameterTypes);
				if (method1 != null) {
					return method1;
				}
			}
		}
		return getMethod(type, method);
	}

	/**
	 * 获取type的method(包括父类)
	 *
	 * @param type   对象类型
	 * @param method
	 * @return method or null
	 */
	public static Method getMethod(Class<?> type, String method, Class<?>[] parameterTypes) {
		while (!type.equals(Object.class)) {
			try {
				return type.getDeclaredMethod(method, parameterTypes);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			type = type.getSuperclass();
		}
		return null;
	}

	/**
	 * 获取type的field(包括父类)
	 *
	 * @param type  对象类型
	 * @param field 不支持‘.’路径
	 * @return field or null
	 */
	public static Field getField(Class<?> type, String field) {
		while (!type.equals(Object.class)) {
			try {
				return type.getDeclaredField(field);
			} catch (NoSuchFieldException e) {
				//e.printStackTrace();
			} catch (SecurityException e) {
				//e.printStackTrace();
			}
			type = type.getSuperclass();
		}
		return null;
	}

	/**
	 * 深层获取类型type的属性path的Field(包括父类)（内部递归算法）
	 */
	private static Field getField(Class<?> type, String[] path, int index) throws Exception {
		Field field = getField(type, path[index]);
		if (field == null) return null;
		if (path.length == index + 1) {
			return field;
		} else if (path.length > 0) {
			return getField(field.getType(), path, index + 1);
		}
		return field;
	}

	/**
	 * 获取字段 Field(包括父类)
	 *
	 * @param model 操作对象
	 * @param field 支持‘.’路径 如 person.name
	 * @return Field
	 * @throws Exception
	 */
	public static Field getField(Object model, String field) throws Exception {
		return getField(model.getClass(), field.split("\\."), 0);
	}

	/**
	 * 获取字段 Field(包括父类)
	 *
	 * @param model 操作对象
	 * @param field 支持‘.’路径 如 person.name
	 * @return Field
	 */
	public static Field getFieldNoException(Object model, String field) {
		try {
			return getField(model.getClass(), field.split("\\."), 0);
		} catch (Throwable e) {
			return null;
		}
	}

	/**
	 * 获取对象obj的属性path的value （内部递归算法）
	 */
	private static void invokeMember(Class<?> type, String[] path, Object obj,
									 Object value, int index) throws Exception {
		Field field = getField(type, path[index]);
		if (field == null) return;
		if (path.length == index + 1) {
			field.setAccessible(true);
			field.set(obj, value);
		} else if (path.length > 0) {
			value = field.get(obj);
			invokeMember(obj.getClass(), path, obj, value, index + 1);
		}
	}

	/**
	 * 获取对象obj的属性path的value （内部递归算法）
	 */
	private static Object invokeMember(Class<?> type, String[] path, Object obj, int index) throws Exception {
		Field field = getField(type, path[index]);
		if (field == null) return null;
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
	 *
	 * @param obj   操作对象
	 * @param field 支持‘.’路径 如 person.name
	 * @throws Exception 数组越界
	 */
	public static void setMember(Object obj, String field, Object value) throws Exception {
		invokeMember(obj.getClass(), field.split("\\."), obj, value, 0);
	}

	/**
	 * 获取 obj 属性 field 的值
	 *
	 * @param obj   操作对象
	 * @param field 支持‘.’路径 如 person.name
	 * @return 成功失败
	 */
	public static boolean setMemberNoException(Object obj, String field, Object value) {
		try {
			invokeMember(obj.getClass(), field.split("\\."), obj, value, 0);
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	/**
	 * 获取 obj 属性 field 的值
	 *
	 * @param obj   操作对象
	 * @param field 支持‘.’路径 如 person.name
	 * @return 值
	 * @throws Exception 数组越界
	 */
	public static Object getMember(Object obj, String field) throws Exception {
		return invokeMember(obj.getClass(), field.split("\\."), obj, 0);
	}

	/**
	 * 获取 obj 属性 field 的值
	 *
	 * @param obj   操作对象
	 * @param field 支持‘.’路径 如 person.name
	 * @return 值
	 */
	public static Object getMemberNoException(Object obj, String field) {
		try {
			return invokeMember(obj.getClass(), field.split("\\."), obj, 0);
		} catch (Throwable e) {
			return null;
		}
	}

	/**
	 * 获取 obj 属性 field 的值
	 *
	 * @param obj
	 * @param field 支持‘.’路径 如 person.name
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws Exception                数组越界
	 */
	public static <T> T getMember(Object obj, String field, Class<T> type) throws Exception {
		obj = invokeMember(obj.getClass(), field.split("\\."), obj, 0);
		if (type.isInstance(obj)) {
			type.cast(obj);
		}
		return null;
	}

	/**
	 * 获取 obj 属性 field 的值
	 *
	 * @param obj
	 * @param field 支持‘.’路径 如 person.name
	 * @return
	 */
	public static <T> T getMemberNoException(Object obj, String field, Class<T> type) {
		try {
			return type.cast(invokeMember(obj.getClass(), field.split("\\."), obj, 0));
		} catch (Throwable e) {
			return null;
		}
	}

	public static Object doMethod(Object obj, String smethod, Object... args) {
		Method method = getMethod(obj.getClass(), smethod, args);
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T doMethod(Object obj, String smethod, Class<T> type, Object... args) {
		Method method = getMethod(obj.getClass(), smethod, args);
		try {
			return type.cast(method.invoke(obj, args));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object doStaticMethod(Class<?> type, String smethod, Object... args) {
		Method method = getMethod(type, smethod, args);
		return doStaticMethod(type, method, args);
	}


	public static <T> T doStaticMethod(Class<?> type, String smethod, Class<T> rettype, Object... args) {
		Method method = getMethod(type, smethod, args);
		return rettype.cast(doStaticMethod(type, method, args));
	}

	public static <T> Object doStaticMethod(Class<?> type, Method method, Object... args) {
		try {
			return method.invoke(type, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 为 type 创建实例
	 * @param type 类型
	 * @param <T> 模板参数
	 * @return 新的实例 失败会 null （很少会失败）
	 */
	public static <T> T newInstance(Class<T> type) {
		try {
			return type.newInstance();
		} catch (Throwable e) {
		}
		try {
			return (T) UnsafeAllocator.create().newInstance(type);
		} catch (Throwable e) {
		}
		return null;
	}

}
