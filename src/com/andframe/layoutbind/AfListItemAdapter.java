package com.andframe.layoutbind;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.activity.framework.AfView;
import com.andframe.adapter.AfListAdapter.IListItem;

/**
 * 相似列表布局适配器
 * @author 树朾
 * @param <T> 实际需要布局模板
 * @param <TT> 可以适配的模板
 */
public abstract class AfListItemAdapter<T,TT> implements IListItem<T> {
	
	private IListItem<TT> item;
	/**
	 * 一个可以适配页面的布局
	 */
	public AfListItemAdapter(IListItem<TT> item) {
		this.item = item;
	}

	@Override
	public void onHandle(AfView view) {
		item.onHandle(view);
	}

	@Override
	public void onBinding(T model, int index) {
		item.onBinding(this.convert(model), index);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
		return item.onCreateView(inflater, parent);
	}

	/**
	 * 把模板数据 T 转成 TT
	 */
	protected abstract TT convert(T model);
}
