package com.andframe.widget.treeview;

import java.util.Collection;

/***
 * AfTreeNodeable 树形构造器
 * @author 树朾
 * 	适用与已有的树形结构转换
 * @param <T>
 */
public interface AfTreeNodeable<T>{
	Collection<T> getChildren(T model);
}
