package com.andframe.widget.treeview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.ListItem;
import com.andframe.widget.multichoice.AfMultiChoiceAdapter;
import com.andframe.widget.multichoice.AfMultiChoiceItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public abstract class AfTreeViewAdapter<T> extends AfMultiChoiceAdapter<T> {


	public interface AfTreeNodeClickable<T>{
		boolean isItemClickable(AfTreeNode<T> item);
	}

	protected List<T> mltOriginData = null;
	protected boolean mDefaultExpanded = false;
	protected AfTreeNode<T> mRootNode = null;
	protected AfTreeNode<T> mLastSelectNode = null;
	protected AfTreeEstablisher<T> mEstablisher = null;
	protected AfTreeNodeClickable<T> mTreeNodeClickable = null;
	protected List<AfTreeNode<T>> mNodeShow = new ArrayList<>();

	protected abstract AfTreeViewItem<T> getTreeViewItem(T data);

	public AfTreeViewAdapter(Context context, List<T> ltdata, AfTreeEstablisher<T> establisher) {
		this(context, ltdata, establisher, false);
	}
	
	public AfTreeViewAdapter(Context context, List<T> ltdata, AfTreeEstablisher<T> establisher,boolean isExpanded) {
		super(context, new ArrayList<>());
		mltOriginData = ltdata;
		mEstablisher = establisher;
		mDefaultExpanded = isExpanded;
		//构造树形
		mRootNode = mEstablisher.establish(ltdata,isExpanded);
		//将树形显示到列表
		establishNodeListToShow(mltArray, mNodeShow, mRootNode);
	}
	
	public void setTreeNodeClickable(AfTreeNodeClickable<T> clickable) {
		this.mTreeNodeClickable = clickable;
	}

	@Override
	protected final AfMultiChoiceItem<T> getMultiChoiceItem(T data) {
		return getTreeViewItem(data);
	}

	public boolean isItemClickable(int index) {
		return mTreeNodeClickable != null && mTreeNodeClickable.isItemClickable(mNodeShow.get(index));
	}
	
	@Override
	protected View onInflateItem(ListItem<T> item, ViewGroup parent) {
		View view = super.onInflateItem(item, parent);
		return ((AfTreeViewItem<T>)item).inflateLayout(view, this);
	}
	
	@Override
	protected boolean bindingItem(View view, ListItem<T> item, int index) {
		AfTreeViewItem<T> tvitem = (AfTreeViewItem<T>)item;
		AfTreeNode<T> node = mNodeShow.get(index);
		tvitem.setNode(node);
//		return super.bindingItem(item, index);
		/**
		 * 添加树形多选 2016-7-1
		 */
		AfMultiChoiceItem.SelectStatus status = AfMultiChoiceItem.SelectStatus.NONE;
		if(isMultiChoiceMode()){
			if(node.isSelected){
				status = AfMultiChoiceItem.SelectStatus.SELECTED;
			}else{
				status = AfMultiChoiceItem.SelectStatus.UNSELECT;
			}
		}
		tvitem.setSelectStatus(node.value, status);
		tvitem.onBinding(view, node.value, index);
		return true;
	}
	
	@Override
	public void onItemClick(int index) {
		AfTreeNode<T> node = mNodeShow.get(index);
		if (isMultiChoiceMode()) {
			/**
			 * 添加树形多选 2016-7-1
			 */
			AfTreeViewItem<T> item = getTreeViewItem(node.value);
			item.setNode(node);
			if (item.isCanSelect(node.value, index)) {
				int count = mChoiceNumber;
				if (mIsSingle) {
					if (mLastSelectNode != null) {
						mLastSelectNode.isSelected = false;
					}
					count = 1;
					mLastSelectNode = node;
					mLastSelectNode.isSelected = true;
				} else {
					node.isSelected = !node.isSelected;
					count += node.isSelected ? 1 : -1;
				}
				super.onItemClick(index);
				mChoiceNumber = count;
			} else {
				node.isExpanded = !node.isExpanded;
				updateNodeListToShow();
			}
		} else {
			node.isExpanded = !node.isExpanded;
			updateNodeListToShow();
		}
	}

	public void expandNode(AfTreeNode<T> node) {
		if (!node.isExpanded) {
			node.isExpanded = true;
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
		if(node.children != null && !node.children.isEmpty()){
			for (AfTreeNode<T> element : node.children) {
				setNodeRecursion(element,expand);
			}
		}
		if(node.level >= 0){
			node.isExpanded = expand;
		}
	}

	@Override
	public boolean addAll(@NonNull Collection<? extends T> ltdata) {
		boolean ret = mltOriginData.addAll(ltdata);
		mRootNode = mEstablisher.establish(mltOriginData,mDefaultExpanded);
		restoreTreeNode(mRootNode, mNodeShow);
		updateNodeListToShow();
		return ret;
	}

	@Override
	public void set(List<T> ltdata) {
		mltOriginData = new ArrayList<>(ltdata);
		mRootNode = mEstablisher.establish(mltOriginData,mDefaultExpanded);
		updateNodeListToShow();
	}
	
	@Override
	public T set(int index, T obj) {
		if (mltOriginData.size() > index) {
			T model = mltOriginData.set(index, obj);
			mRootNode = mEstablisher.establish(mltOriginData,mDefaultExpanded);
			updateNodeListToShow();
			return model;
		}
		return null;
	}
	
	@Override
	public void add(int index, T object) {
		if (mltOriginData.size() >= index) {
			mltOriginData.add(index, object);
			mRootNode = mEstablisher.establish(mltOriginData,mDefaultExpanded);
			updateNodeListToShow();
		}
	}
	
	@Override
	public T remove(int index) {
		if (mltOriginData.size() > index) {
			T model = mltOriginData.remove(index);
			mRootNode = mEstablisher.establish(mltOriginData,mDefaultExpanded);
			updateNodeListToShow();
			return model;
		}
		return null;
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
		restoreTreeNode(mRootNode,mNodeShow);
		mltArray.clear();
		mNodeShow.clear();
		/**
		 * 添加树形多选 2016-7-1
		 */
//		closeMultiChoice();
		establishNodeListToShow(mltArray, mNodeShow, mRootNode);
		if (isMultiChoiceMode()) {
			super.restoreSelect();
		} else {
			notifyDataSetChanged();
		}
	}

	protected void restoreTreeNode(AfTreeNode<T> root,List<AfTreeNode<T>> nodes) {
		List<AfTreeNode<T>> remain = new ArrayList<>(nodes);
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
				restoreTreeNode(child, remain);
			}
		}
	}


	//region 树形多选
	/**
	 * 添加树形多选 2016-7-1
	 */

	@Override
	public void restoreSelect() {
		if (isMultiChoiceMode()) {
			restoreSelect(mRootNode);
		}
		super.restoreSelect();
	}

	private void restoreSelect(AfTreeNode<T> node) {
		node.isSelected = false;
		if (node.children != null && node.children.size() > 0) {
			for (AfTreeNode<T> child : node.children) {
				restoreSelect(child);
			}
		}
	}

	public boolean beginMultiChoice(int index, boolean notify) {
		if (!isMultiChoiceMode() && getCount() > 0) {
			restoreSelect(mRootNode);
			if (index > -1 && index < mNodeShow.size()) {
				AfTreeNode<T> node = mNodeShow.get(index);
				node.isSelected = true;
				mChoiceNumber = 1;
			}
		}
		return super.beginMultiChoice(index, notify);
	}

	@Override
	public boolean closeMultiChoice() {
		if (super.closeMultiChoice()) {
			mLastSelectNode = null;
			return true;
		}
		return false;
	}

	@Override
	public List<T> getSelectedItems() {
		List<T> list = new ArrayList<>();
		if(isMultiChoiceMode()) {
			peekSelectedItems(list, mRootNode);
			closeMultiChoice();
		}
		return list;
	}

	@Override
	public List<T> peekSelectedItems() {
		List<T> list = new ArrayList<>();
		if(isMultiChoiceMode()) {
			peekSelectedItems(list, mRootNode);
		}
		return list;
	}

	private void peekSelectedItems(List<T> list, AfTreeNode<T> node) {
		if (node.isSelected) {
			list.add(node.value);
		}
		if (node.children != null && node.children.size() > 0) {
			for (AfTreeNode<T> child : node.children) {
				peekSelectedItems(list, child);
			}
		}
	}
	//endregion

}
