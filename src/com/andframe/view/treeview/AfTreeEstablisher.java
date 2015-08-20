package com.andframe.view.treeview;

import java.util.Collection;

public class AfTreeEstablisher<T>{

	protected AfTreeNodeable<T> mNodeable = null;
	protected AfTreeEstablishable<T> mEstablishable = null;

	public AfTreeEstablisher(AfTreeNodeable<T> nodeable) {
		mNodeable = nodeable;
	}
	
	public AfTreeEstablisher(AfTreeEstablishable<T> establishable) {
		mEstablishable = establishable;
	}
	
	AfTreeNode<T> establish(Collection<T> collect,boolean isExpanded){
		if(mNodeable != null){
			return establish(collect,mNodeable,isExpanded);
		}else if (mEstablishable != null) {
			return establish(collect,mEstablishable,isExpanded);
		}
		throw new NullPointerException("AfTreeable = null");
	}

	AfTreeNode<T> establish(Collection<T> collect,AfTreeNodeable<T> able,boolean isExpanded) {
		return new AfTreeNode<T>(collect, able,isExpanded);
	}

	AfTreeNode<T> establish(Collection<T> collect,AfTreeEstablishable<T> able,boolean isExpanded) {
		return new AfTreeNode<T>(collect, able,isExpanded);
	}

}
