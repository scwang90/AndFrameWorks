package com.andframe.widget.tree;

/***
 * TreeResolver 树形解析器
 * 	适用与已有的树形结构转换
 * @author 树朾
 * @param <T>
 */
public interface TreeResolver<T>{
	Iterable<T> getChildren(T model);
}
