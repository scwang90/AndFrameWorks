package com.andframe.widget.tree;

/***
 * TreeBuilder 树形构造器
 * 适用于 将 list 转为 tree
 * @author 树朾
 * @param <T>
 */
public interface TreeJudge<T> {
	boolean isChildOf(T child, T parent);
}
