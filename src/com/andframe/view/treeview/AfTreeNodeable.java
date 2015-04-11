package com.andframe.view.treeview;

import java.util.Collection;

/***
 * AfTreeNodeable 树形构造器
 * @author SCWANG
 *		适用与已有的树形结构转换
 * @param <T>
 */
public interface AfTreeNodeable<T>{
	Collection<T> getChildren(T model);
}
