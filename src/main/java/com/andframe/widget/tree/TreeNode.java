package com.andframe.widget.tree;

import com.andframe.$;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class TreeNode<T> {

	public T value = null;// 该节点的值
	public int level = 0;// 该节点的值
	public boolean isExpanded = false;// 该节点是否展开
	public boolean isSelected = false;// 该节点是否选择
	public List<TreeNode<T>> children = null;

	public TreeNode(TreeNode<T> parent, T model, boolean isExpanded) {
		this.value = model;
		this.level = parent.level + 1;
		this.isExpanded = isExpanded;
	}
	
	TreeNode(Collection<? extends T> collect, TreeJudge<T> builder, boolean isExpanded){
		this.level = -1;
		this.isExpanded = true;
		List<TreeNode<T>> nodes = $.query(collect).map(m -> new TreeNode<>(this, m, isExpanded)).toList();
		List<TreeNode<T>> backs = new LinkedList<>(nodes);
		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).fillChildNode(backs, builder);
		}
		this.children = backs;
	}


	TreeNode(Collection<? extends T> collect, TreeResolver<T> able, boolean isExpanded) {
		this.level = -1;
		this.isExpanded = true;
		this.children = new ArrayList<>();
		for (T model : collect) {
			children.add(new TreeNode<>(this, model, able, isExpanded));
		}
	}

	private TreeNode(TreeNode<T> parent, T model, TreeResolver<T> able, boolean isExpanded) {
		this(parent,model,isExpanded);
		Iterable<T> children = able.getChildren(model);
		if(children != null){
			this.children = new ArrayList<>();
			for (T child : children) {
				this.children.add(new TreeNode<>(this, child, able, isExpanded));
			}
		}
	}

	private int fillChildNode(List<TreeNode<T>> list, TreeJudge<T> builder) {
		children = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			TreeNode<T> node = list.get(i);
			if (node.level == 0 && node != this && builder.isChildOf(node.value, value)) {
				node.level = this.level + 1;
				children.add(node);
				list.remove(i--);
			}
		}
		if(children.size() > 0){
			int childCount = 0;
			for (TreeNode<T> child : children) {
				childCount += child.fillChildNode(list, builder);
			}
			return children.size() + childCount;
		} else {
			children = null;
			return 0;
		}
	}

}
