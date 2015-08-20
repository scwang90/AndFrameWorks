package com.andframe.util.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * java堆栈工具类 1.可以查看当前函数名称 2.可以判断当前函数是否已经递归调用
 * @author 树朾
 */
public class AfStackTrace {

	/**
	 *  获取调用 getCurrentStatck 的 Statck
	 * @author 树朾
	 * @return 调用getCurrentStatck的Statck
	 */
	public static StackTraceElement getCurrentStatck() {
		int index = 1;
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		StackTraceElement stack = stacks[index];
		if (!stack.getClassName().equals(AfStackTrace.class.getName())) {
			for (int i = 0; i < stacks.length; i++) {
				if (AfStackTrace.class.getName().equals(
						stacks[i].getClassName())) {
					index = i;
					stack = stacks[i];
					break;
				}
			}
		}
		index++;
		stack = stacks[index];
		return stack;
	}

	/**
	 *  获取调用 getCurrentStatck 的 Method
	 * 暂时不支持重载方法
	 * @author 树朾
	 * @return 调用getCurrentStatck的Statck
	 */
	public static Method getCurrentMethod() {
		try {
			StackTraceElement stack = new Throwable().getStackTrace()[1];
			String methodName = stack.getMethodName();
			for (Method method : Class.forName(stack.getClassName()).getMethods()) {
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
	 *  获取调用 getCurrentStatck 的 Method
	 * 暂时不支持重载方法
	 * @author 树朾
	 * @return 调用getCurrentStatck的Statck
	 */
	public static <T extends Annotation> T getCurrentMethodAnnotation(Class<T> annotation) {
		try {
			StackTraceElement stack = new Throwable().getStackTrace()[1];
			String methodName = stack.getMethodName();
			for (Method method : Class.forName(stack.getClassName()).getMethods()) {
				if (method.getName().endsWith(methodName)) {
					return method.getAnnotation(annotation);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *  判断调用isLoopCall 的方法是否已经被循环递归调用
	 * @author 树朾
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
