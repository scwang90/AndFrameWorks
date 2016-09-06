package com.andframe.util.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * java堆栈工具类
 * 1.可以查看当前函数名称
 * 2.可以判断当前函数是否已经递归调用
 * @author 树朾
 */
@SuppressWarnings("unused")
public class AfStackTrace {

	/**
	 *  获取调用 getCurrentStatck 的 Statck
	 * @return 调用getCurrentStatck的Statck
	 */
	public static StackTraceElement getCurrentStatck() {
		int index = 1;
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		StackTraceElement stack = stacks[index];
		if (!stack.getClassName().equals(AfStackTrace.class.getName())) {
			for (int i = 0; i < stacks.length; i++) {
				if (AfStackTrace.class.getName().equals(stacks[i].getClassName())) {
					index = i;
					break;
				}
			}
		}
		index++;
		stack = stacks[index];
		return stack;
	}

	/**
	 *  获取调用 getCurrentStatck 的 Statck
	 * @param level 向上层数 默认为0
	 * @return 调用getCurrentStatck的Statck
	 */
	public static StackTraceElement getCurrentStatck(int level) {
		int index = 1;
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		StackTraceElement stack = stacks[index];
		if (!stack.getClassName().equals(AfStackTrace.class.getName())) {
			for (int i = 0; i < stacks.length; i++) {
				if (AfStackTrace.class.getName().equals(stacks[i].getClassName())) {
					index = i;
					break;
				}
			}
		}
		index++;
		stack = stacks[index + level];
		return stack;
	}

	/**
	 *  获取调用 getCurrentStatck 的 Method
	 * 暂时不支持重载方法
	 * @return 调用getCurrentStatck的Statck
	 */
	public static Method getCurrentMethod() {
		return getCurrentMethod(0);
	}

	/**
	 * 获取调用 getCurrentStatck 的 Method
	 * 暂时不支持重载方法
	 * @param level 向上层数 默认为0
	 * @return 调用getCurrentStatck的Statck
	 */
	public static Method getCurrentMethod(int level) {
		try {
			StackTraceElement stack = new Throwable().getStackTrace()[1 + level];
			String methodName = stack.getMethodName();
			if (methodName.endsWith("$override")) {
				methodName = methodName.substring(0,methodName.length()-"$override".length());
			}
			for (Method method : Class.forName(stack.getClassName()).getDeclaredMethods()) {
				if (method.getName().endsWith(methodName)) {
					return method;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取调用 getCurrentMethodAnnotation 的 Method 的 Annotation注解
	 * 暂时不支持重载方法
	 * @param annotation 注解
	 * @return Method 的 Annotation
	 */
	public static <T extends Annotation> T getCurrentMethodAnnotation(Class<T> annotation) {
		return getCurrentMethodAnnotation(annotation,0);
	}

	/**
	 * 获取调用 getCurrentMethodAnnotation 的 Method 的 Annotation注解
	 * 暂时不支持重载方法
	 * @param annotation 注解
	 * @param level 向上层数 默认为0
	 * @return Method 的 Annotation
	 */
	public static <T extends Annotation> T getCurrentMethodAnnotation(Class<T> annotation, int level) {
		try {
			StackTraceElement stack = new Throwable().getStackTrace()[1 + level];
			String methodName = stack.getMethodName();
			Class clazz = null;
			try {
				clazz = Class.forName(stack.getClassName());
			} catch (ClassNotFoundException e) {
				String tag = ".override";
				String clazzName = stack.getClassName();
				if (clazzName.endsWith(tag)) {
					clazz = Class.forName(clazzName.substring(0, clazzName.length() - tag.length()));
				} else {
					e.printStackTrace();
				}
			}
			if (clazz != null) {
				for (Method method : clazz.getDeclaredMethods()) {
					if (method.getName().endsWith(methodName)) {
						return method.getAnnotation(annotation);
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取调用 getCurrentMethodClassAnnotation 的 Method 的 Class的 Annotation注解
	 * 暂时不支持重载方法
	 * @param annotation 注解
	 * @return Method 的 Class的 的 Annotation
	 */
	public static <T extends Annotation> T getCurrentMethodClassAnnotation(Class<T> annotation) {
		return getCurrentMethodClassAnnotation(annotation,0);
	}

	/**
	 * 获取调用 getCurrentMethodClassAnnotation 的 Method 的 Class的 Annotation注解
	 * 暂时不支持重载方法
	 * @param annotation 注解
	 * @param level 向上层数 默认为0
	 * @return Method 的 Class的 的 Annotation
	 */
	public static <T extends Annotation> T getCurrentMethodClassAnnotation(Class<T> annotation, int level) {
		try {
			StackTraceElement stack = new Throwable().getStackTrace()[1 + level];
			Class<?> clazz = Class.forName(stack.getClassName());
			if (clazz.isAnnotationPresent(annotation)) {
				return clazz.getAnnotation(annotation);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *  判断调用isLoopCall 的方法是否已经被循环递归调用
	 * @return true 已经递归 false 没有递归
	 */
	public static boolean isLoopCall() {
		int index = 1;
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		StackTraceElement stack = stacks[index];
		if (!stack.getClassName().equals(AfStackTrace.class.getName())) {
			for (int i = 0; i < stacks.length; i++) {
				if (AfStackTrace.class.getName().equals(
						stacks[i].getClassName())) {
					index = i;
					break;
				}
			}
		}
		index++;
		stack = stacks[index];
		for (int i = index + 1; i < stacks.length; i++) {
			StackTraceElement element = stacks[i];
			if (element.getClassName().equals(stack.getClassName())
					&& element.getMethodName().equals(stack.getMethodName())) {
				// System.out.println("递归检测");
				return true;
			}
		}
		return false;
	}
}
