package com.andframe.widget.tree;

import java.util.Collection;

public class TreeBuilder<T>{

	private TreeJudge<T> treeJudge = null;
	private TreeResolver<T> treeResolver = null;

	public TreeBuilder(TreeJudge<T> judge) {
		treeJudge = judge;
	}

	public TreeBuilder(TreeResolver<T> resolver) {
		treeResolver = resolver;
	}

	
	TreeNode<T> establish(Collection<? extends T> collect, boolean isExpanded){
		if(treeResolver != null){
			return establish(collect, treeResolver,isExpanded);
		}else if (treeJudge != null) {
			return establish(collect, treeJudge,isExpanded);
		}
		throw new NullPointerException("AfTreeable = null");
	}

	private TreeNode<T> establish(Collection<? extends T> collect, TreeResolver<T> able, boolean isExpanded) {
		return new TreeNode<T>(collect, able,isExpanded);
	}

	private TreeNode<T> establish(Collection<? extends T> collect, TreeJudge<T> able, boolean isExpanded) {
		return new TreeNode<T>(collect, able,isExpanded);
	}

}
