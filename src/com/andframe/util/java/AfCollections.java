package com.andframe.util.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 集合工具类
 * AfCollections
 * @author 树朾
 */
public class AfCollections {

	public interface Comparable<T>{
		boolean equals(T left,T right);
	}
	
	public static class ComparableImpl<TT> implements Comparable<TT>{
		@Override
		public boolean equals(TT left, TT right) {
			// TODO Auto-generated method stub
			return (left != null && right != null) && left.equals(right);
		}
	}

	/**
	 * 获取在 left 中 right 不存在的元素
	 * @param left（传入参数，不会改变）
	 * @param right（传入参数，不会改变）
	 * @return 与 left right 独立的 list
	 */
	public static <T> List<T> redundance(List<T> left, List<T> right) {
		return redundance(left, right, new ComparableImpl<T>());
	}

	/**
	 * 获取在 left 中 right 不存在的元素
	 * @param left（传入参数，不会改变）
	 * @param right（传入参数，不会改变）
	 * @param comparable 比较器
	 * @return 与 left right 独立的 list
	 */
	public static <T> List<T> redundance(List<T> left, List<T> right,Comparable<T> comparable) {
		// TODO Auto-generated method stub
		List<T> redundance = new ArrayList<T>();
		List<T> tright = new ArrayList<T>(right);
		for (T f : left) {
			boolean find = false;
			for (int i = 0; i < tright.size(); i++) {
				if (comparable.equals(f, tright.get(i))) {
					find = true;
					tright.remove(i);
					break;
				}
			}
			if (!find) {
				redundance.add(f);
			}
		}
		return redundance;
	}
	
	/**
	 * 在 left 中添加 right 中的所有元素（并排除重复）
	 * @param left （传入参数，删除成功元素将会增多）
	 * @param right（传入参数，不会改变）
	 */
	public static <T> void addall(List<T> left, List<T> right) {
		// TODO Auto-generated method stub
		addall(left, right, new ComparableImpl<T>());
	}
	
	/**
	 * 在 left 中添加 right 中的所有元素（并排除重复）
	 * @param left （传入参数，删除成功元素将会增多）
	 * @param right（传入参数，不会改变）
	 * @param comparable 比较器
	 */
	public static <T> void addall(List<T> left, List<T> right, Comparable<T> comparable) {
		List<T> redundance = redundance(right, left,comparable);
		left.addAll(redundance);
	}
	
	/**
	 * 在 left 中删除 right 中的所有元素
	 * @param left （传入参数，删除成功元素将会减少）
	 * @param right（传入参数，不会改变）
	 */
	public static <T> void remove(List<T> left, List<T> right) {
		// TODO Auto-generated method stub
		remove(left, right, new ComparableImpl<T>());
	}

	/**
	 * 在 left 中删除 right 中的所有元素
	 * @param left （传入参数，删除成功元素将会减少）
	 * @param right（传入参数，不会改变）
	 * @param comparable 
	 */
	public static <T> void remove(List<T> left, List<T> right, Comparable<T> comparable) {
		for (T r : right) {
			for (int i = 0; i < left.size(); i++) {
				if (comparable.equals(left.get(i), r)) {
					left.remove(i);
					break;
				}
			}
		}
	}

	/**
	 * 判断集合 collects 是否为空
	 * @param collects
	 * @return collects == null || collects.size() == 0
	 */
	public static boolean isEmpty(Collection<?> collects) {
		// TODO Auto-generated method stub
		return collects == null || collects.size() == 0;
	}

	/**
	 * 判断集合 collects 是否不为空
	 * @param collects
	 * @return collects == null || collects.size() == 0
	 */
	public static boolean isNotEmpty(Collection<?> collects) {
		// TODO Auto-generated method stub
		return collects != null && collects.size() > 0;
	}
	
	/**
	 * 比较 left right 中的元素是否相等 包括 顺序
	 * @param left（传入参数，不会改变）
	 * @param right（传入参数，不会改变）
	 * @return 相等 true 否则 false
	 */
	public static <T> boolean equals(List<T> left,List<T> right) {
		// TODO Auto-generated method stub
		return equals(left, right,new ComparableImpl<T>());
	}

	/**
	 * 比较 left right 中的元素是否相等 包括 顺序
	 * @param left（传入参数，不会改变）
	 * @param right（传入参数，不会改变）
	 * @param comparable 比较器
	 * @return 相等 true 否则 false
	 */
	public static <T> boolean equals(List<T> left,List<T> right, Comparable<T> comparable) {
		if (left != right) {
			if (left != null && right != null && left.size() == right.size()) {
				for (int i = 0,len = left.size(); i < len; i++) {
					if (!comparable.equals(left.get(i), right.get(i))) {
						return false;
					}
				}
				return true;
			}
			return false;
		}
		return true;
	}
}
