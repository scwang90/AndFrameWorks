package com.andframe.widget.tree;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.andframe.api.adapter.ItemViewer;
import com.andframe.widget.select.SelectListItemAdapter;
import com.andframe.widget.select.SelectListItemViewer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public abstract class TreeViewItemAdapter<T> extends SelectListItemAdapter<T> implements AdapterView.OnItemClickListener {

	public interface AfTreeNodeClickable<T>{
		boolean isItemClickable(TreeNode<T> item);
	}

	protected List<T> mltOriginData = null;
	protected boolean mDefaultExpanded = false;
	protected TreeNode<T> mRootNode = null;
	protected TreeNode<T> mLastSelectNode = null;
	protected TreeBuilder<T> mTreeBuilder = null;
	protected AfTreeNodeClickable<T> mTreeNodeClickable = null;
	protected List<TreeNode<T>> mNodeShow = new ArrayList<>();

	protected abstract SelectTreeItemViewer<T> newTreeViewItem(int viewType);

	public TreeViewItemAdapter(@Nullable List<T> list, TreeBuilder<T> builder) {
		this(list, builder, false);
	}

	public TreeViewItemAdapter(@Nullable List<T> list, TreeBuilder<T> builder, boolean isExpanded) {
		super(new ArrayList<>());
		if (list == null) {
			list = new ArrayList<>();
		}
		mltOriginData = list;
		mTreeBuilder = builder;
		mDefaultExpanded = isExpanded;
		//构造树形
		mRootNode = mTreeBuilder.establish(list, isExpanded);
		//将树形显示到列表
		establishNodeListToShow(mltArray, mNodeShow, mRootNode);
	}
	
	public void setTreeNodeClickable(AfTreeNodeClickable<T> clickable) {
		this.mTreeNodeClickable = clickable;
	}

	@Override
	protected SelectListItemViewer<T> newSelectItem(int viewType) {
		return newTreeViewItem(viewType);
	}

	public boolean isItemClickable(int index) {
		return mTreeNodeClickable != null && mTreeNodeClickable.isItemClickable(mNodeShow.get(index));
	}
	
	@NonNull
	@Override
	public View inflateItem(ItemViewer<T> item, ViewGroup parent) {
		View view = super.inflateItem(item, parent);
		if (item instanceof SelectTreeItemViewer) {
			return ((SelectTreeItemViewer<T>)item).inflateLayout(view, this);
		}
		return view;
	}
	
	@Override
	public void bindingItem(View view, ItemViewer<T> item, int index) {
		SelectTreeItemViewer<T> tvitem = (SelectTreeItemViewer<T>)item;
		TreeNode<T> node = mNodeShow.get(index);
		tvitem.setNode(node);
//		return super.bindingItem(item, index);
		/*
		 * 添加树形多选 2016-7-1
		 */
		SelectListItemViewer.SelectStatus status = SelectListItemViewer.SelectStatus.NONE;
		if(isSelectMode()){
			if(node.isSelected){
				status = SelectListItemViewer.SelectStatus.SELECTED;
			}else{
				status = SelectListItemViewer.SelectStatus.UN_SELECT;
			}
		}
		tvitem.setSelectStatus(node.value, status);
 		tvitem.onBinding(view, node.value, index);
	}


	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		onItemClick(i);
	}
	
	@Override
	public void onItemClick(int index) {
		TreeNode<T> node = mNodeShow.get(index);
		if (isSelectMode() && isCanSelect(node.value, index)) {
			/**
			 * 添加树形多选 2016-7-1
			 */
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
	}

	protected boolean isCanSelect(T value, int index) {
		TreeNode<T> node = mNodeShow.get(index);
		return node == null || node.children == null || node.children.size() == 0;
	}

	public TreeNode<T> getNode(int index) {
		return mNodeShow.get(index);
	}

	public void expandNode(TreeNode<T> node) {
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

	private void setNodeRecursion(TreeNode<T> node, boolean expand) {
		if(node.children != null && !node.children.isEmpty()){
			for (TreeNode<T> element : node.children) {
				setNodeRecursion(element,expand);
			}
		}
		if(node.level >= 0){
			node.isExpanded = expand;
		}
	}

	@Override
	public void clear() {
		super.clear();
		mltOriginData.clear();
		mRootNode = mTreeBuilder.establish(mltOriginData,mDefaultExpanded);
		updateNodeListToShow();
	}

	@Override
	public boolean addAll(@NonNull Collection<? extends T> list) {
		boolean ret = mltOriginData.addAll(list);
		mRootNode = mTreeBuilder.establish(mltOriginData,mDefaultExpanded);
		restoreTreeNode(mRootNode, mNodeShow);
		updateNodeListToShow();
		return ret;
	}

	@Override
	public void set(@NonNull List<T> list) {
		mltOriginData = new ArrayList<>(list);
		mRootNode = mTreeBuilder.establish(mltOriginData,mDefaultExpanded);
		updateNodeListToShow();
	}
	
	@Override
	public T set(int index, T obj) {
		if (mltOriginData.size() > index) {
			T model = mltOriginData.set(index, obj);
			mRootNode = mTreeBuilder.establish(mltOriginData,mDefaultExpanded);
			updateNodeListToShow();
			return model;
		}
		return null;
	}
	
	@Override
	public void add(int index, T object) {
		if (mltOriginData.size() >= index) {
			mltOriginData.add(index, object);
			mRootNode = mTreeBuilder.establish(mltOriginData,mDefaultExpanded);
			updateNodeListToShow();
		}
	}
	
	@Override
	public T remove(int index) {
		if (mltOriginData.size() > index) {
			T model = mltOriginData.remove(index);
			mRootNode = mTreeBuilder.establish(mltOriginData,mDefaultExpanded);
			updateNodeListToShow();
			return model;
		}
		return null;
	}

	// 构造要展示在listview的nodeListToShow
	public void establishNodeListToShow(List<T> values, List<TreeNode<T>> nodes, TreeNode<T> node) {
		if (node.isExpanded && node.children != null) {
			for (TreeNode<T> child : node.children) {
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
		if (isSelectMode()) {
			super.restoreSelect();
		} else {
			notifyDataSetChanged();
		}
	}

	protected void restoreTreeNode(TreeNode<T> root, List<TreeNode<T>> nodes) {
		List<TreeNode<T>> remain = new ArrayList<>(nodes);
		for (TreeNode<T> node : nodes) {
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
			for (TreeNode<T> child : root.children) {
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
		if (isSelectMode()) {
			restoreSelect(mRootNode);
		}
		super.restoreSelect();
	}

	private void restoreSelect(TreeNode<T> node) {
		node.isSelected = false;
		if (node.children != null && node.children.size() > 0) {
			for (TreeNode<T> child : node.children) {
				restoreSelect(child);
			}
		}
	}

	public boolean beginSelectMode(int index, boolean notify) {
		if (!isSelectMode() && getCount() > 0) {
			restoreSelect(mRootNode);
			if (index > -1 && index < mNodeShow.size()) {
				TreeNode<T> node = mNodeShow.get(index);
				node.isSelected = true;
				mChoiceNumber = 1;
			}
		}
		return super.beginSelectMode(index, notify);
	}

	@Override
	public boolean closeSelectMode() {
		if (super.closeSelectMode()) {
			mLastSelectNode = null;
			return true;
		}
		return false;
	}

	@NonNull
    @Override
	public List<T> getSelectedItems() {
		List<T> list = new ArrayList<>();
		if(isSelectMode()) {
			peekSelectedItems(list, mRootNode);
			closeSelectMode();
		}
		return list;
	}

	@NonNull
	@Override
	public List<T> peekSelectedItems() {
		List<T> list = new ArrayList<>();
		if(isSelectMode()) {
			peekSelectedItems(list, mRootNode);
		}
		return list;
	}

	@Override
	public int selectCount() {
		return peekSelectedItems().size();
	}

	@Override
	public T getSelectedItem() {
		List<T> list = getSelectedItems();
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	private void peekSelectedItems(List<T> list, TreeNode<T> node) {
		if (node.isSelected) {
			list.add(node.value);
		}
		if (node.children != null && node.children.size() > 0) {
			for (TreeNode<T> child : node.children) {
				peekSelectedItems(list, child);
			}
		}
	}
	//endregion

}
