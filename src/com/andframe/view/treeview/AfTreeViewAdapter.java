package com.andframe.view.treeview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.view.multichoice.AfMultiChoiceAdapter;
import com.andframe.view.multichoice.AfMultiChoiceItem;

public abstract class AfTreeViewAdapter<T> extends AfMultiChoiceAdapter<T> {
	
	public interface AfTreeNodeClickable<T>{
		boolean isItemClickable(AfTreeNode<T> item);
	}

	protected List<T> mltOriginData = null;
	protected boolean mDefaultExpanded = false;
	protected AfTreeNode<T> mRootNode = null;
	protected AfTreeEstablisher<T> mEstablisher = null;
	protected AfTreeNodeClickable<T> mTreeNodeClickable = null;
	protected List<AfTreeNode<T>> mNodeShow = new ArrayList<AfTreeNode<T>>();

	protected abstract AfTreeViewItem<T> getTreeViewItem(T data);

	public AfTreeViewAdapter(Context context, List<T> ltdata,AfTreeEstablisher<T> establisher) {
		this(context,ltdata,establisher,false);
		// TODO Auto-generated constructor stub
	}
	
	public AfTreeViewAdapter(Context context, List<T> ltdata,AfTreeEstablisher<T> establisher,boolean isExpanded) {
		super(context, new ArrayList<T>());
		// TODO Auto-generated constructor stub
		mltOriginData = ltdata;
		mEstablisher = establisher;
		mDefaultExpanded = isExpanded;
		//构造树形
		mRootNode = mEstablisher.establish(ltdata,isExpanded);
		//将树形显示到列表
		establishNodeListToShow(mltArray,mNodeShow,mRootNode);
	}
	
	public void setTreeNodeClickable(AfTreeNodeClickable<T> clickable) {
		this.mTreeNodeClickable = clickable;
	}

	@Override
	protected final AfMultiChoiceItem<T> getMultiChoiceItem(T data) {
		// TODO Auto-generated method stub
		return getTreeViewItem(data);
	}

	public boolean isItemClickable(int index) {
		// TODO Auto-generated method stub
		if(mTreeNodeClickable == null){
			return false;
		}
		return mTreeNodeClickable.isItemClickable(mNodeShow.get(index));//
	}
	
	@Override
	protected View onInflateItem(IAfLayoutItem<T> item,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = super.onInflateItem(item, parent);
		return ((AfTreeViewItem<T>)item).inflateLayout(view,this);
	}
	
	@Override
	protected boolean bindingItem(IAfLayoutItem<T> item, int index) {
		// TODO Auto-generated method stub
		AfTreeViewItem<T> tvitem = (AfTreeViewItem<T>)item;
		tvitem.setNode(mNodeShow.get(index));
		return super.bindingItem(item, index);
	}
	
	@Override
	public void onItemClick(int index) {
		// TODO Auto-generated method stub
		if(isMultiChoiceMode()){
			super.onItemClick(index);
		}else{
			AfTreeNode<T> node = mNodeShow.get(index);
			node.isExpanded = !node.isExpanded;
			updateNodeListToShow();
		}
	}
	
	public void closeAll() {
		setNodeRecursion(mRootNode,false);
		updateNodeListToShow();
	}
	
	public void expandAll() {
		setNodeRecursion(mRootNode,true);
		updateNodeListToShow();
	}

	private void setNodeRecursion(AfTreeNode<T> node, boolean expand) {
		// TODO Auto-generated method stub
		if(node.children != null && !node.children.equals("")){
			for (AfTreeNode<T> element : node.children) {
				setNodeRecursion(element,expand);
			}
		}
		if(node.level >= 0){
			node.isExpanded = expand;
		}
	}

	@Override
	public void addData(List<T> ltdata) {
		// TODO Auto-generated method stub
		mltOriginData.addAll(ltdata);
		mRootNode = mEstablisher.establish(mltOriginData,mDefaultExpanded);
		restoreTreeNode(mRootNode,mNodeShow);
		updateNodeListToShow();
	}

	@Override
	public void setData(List<T> ltdata) {
		// TODO Auto-generated method stub
		mltOriginData = ltdata;
		mRootNode = mEstablisher.establish(mltOriginData,mDefaultExpanded);
		updateNodeListToShow();
	}
	
	@Override
	public void setData(int index, T obj) {
		// TODO Auto-generated method stub
		if (mltOriginData.size() > index) {
			mltOriginData.set(index, obj);
			mRootNode = mEstablisher.establish(mltOriginData,mDefaultExpanded);
			updateNodeListToShow();
		}
	}
	
	@Override
	public void insert(int index, T object) {
		// TODO Auto-generated method stub
		if (mltOriginData.size() >= index) {
			mltOriginData.add(index, object);
			mRootNode = mEstablisher.establish(mltOriginData,mDefaultExpanded);
			updateNodeListToShow();
		}
	}
	
	@Override
	public void remove(int index) {
		// TODO Auto-generated method stub
		if (mltOriginData.size() > index) {
			mltOriginData.remove(index);
			mRootNode = mEstablisher.establish(mltOriginData,mDefaultExpanded);
			updateNodeListToShow();
		}
	}

	// 构造要展示在listview的nodeListToShow
	public void establishNodeListToShow(List<T> values,List<AfTreeNode<T>> nodes,AfTreeNode<T> node) {
		if (node.isExpanded && node.children != null) {
			for (AfTreeNode<T> child : node.children) {
				nodes.add(child);
				values.add(child.value);
				establishNodeListToShow(values,nodes,child);
			}
		}
	}
	
	protected void updateNodeListToShow() {
		// TODO Auto-generated method stub
		restoreTreeNode(mRootNode,mNodeShow);
		mltArray.clear();
		mNodeShow.clear();
		closeMultiChoice();
		establishNodeListToShow(mltArray,mNodeShow,mRootNode);
		notifyDataSetChanged();
	}

	protected void restoreTreeNode(AfTreeNode<T> root,List<AfTreeNode<T>> nodes) {
		// TODO Auto-generated method stub
		List<AfTreeNode<T>> remain = new ArrayList<AfTreeNode<T>>(nodes);
		for (AfTreeNode<T> node : nodes) {
			if(root == node){
				remain.remove(node);
				break;//找到相同的 说明没有改动 直接结束循环
			}else if(node.value.equals(root.value)){
				remain.remove(node);
				root.isExpanded = node.isExpanded;
				break;//值相等说明要替换
			}
		}
		if(root.children != null && root.children.size() > 0){
			for (AfTreeNode<T> child : root.children) {
				restoreTreeNode(child,remain);
			}
		}
	}

}
