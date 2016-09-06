package com.andframe.widget.treeview;

/***
 * AfTreeEstablishable 树形转换器
 * @author 树朾
 * 适用于 将 list 转为 tree
 * @param <T>
 */
public interface AfTreeEstablishable<T>{
	boolean isTopNode(T model);
	boolean isChildOf(T child, T parent);
}
