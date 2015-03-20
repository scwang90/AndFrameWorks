package com.ontheway.util;

/**
 * @Description: java堆栈工具类
 * 1.可以查看当前函数名称
 * 2.可以判断当前函数是否已经递归调用
 * @Author: scwang
 * @Version: V1.0, 2015-3-13 下午4:58:20
 * @Modified: 初次创建AfStackTrace类
 */
public class AfStackTrace {

	/**
	 * @Description: 获取调用 getCurrentStatck 的Statck
	 * @Author: scwang
	 * @Version: V1.0, 2015-2-26 下午4:21:59
	 * @return 调用getCurrentStatck的Statck
	 */
	public static StackTraceElement getCurrentStatck(){
		int index = 1;
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		StackTraceElement stack = stacks[index];
		if (!stack.getClassName().equals(AfStackTrace.class.getName())) {
			for (int i = 0; i < stacks.length; i++) {
				if (AfStackTrace.class.getName().equals(stacks[i].getClassName())) {
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
	 * @Description: 判断调用isLoopCall 的方法是否已经被循环递归调用
	 * @Author: scwang
	 * @Version: V1.0, 2015-2-26 下午4:18:47
	 * @return true 已经递归 false 没有递归
	 */
	public static boolean isLoopCall() {
		int index = 1;
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		StackTraceElement stack = stacks[index];
		if (!stack.getClassName().equals(AfStackTrace.class.getName())) {
			for (int i = 0; i < stacks.length; i++) {
				if (AfStackTrace.class.getName().equals(stacks[i].getClassName())) {
					index = i;
					stack = stacks[i];
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
				//System.out.println("递归检测");
				return true;
			}
		}
		return false;
	}
}
