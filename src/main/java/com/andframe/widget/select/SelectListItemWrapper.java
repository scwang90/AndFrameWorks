package com.andframe.widget.select;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.adapter.ItemViewer;

/**
 * 多选ITEM模板
 * @param <T>
 */
@SuppressWarnings("unused")
public class SelectListItemWrapper<T> extends SelectListItemViewer<T> {

	protected ItemViewer<T> mWrappedItem;

	public SelectListItemWrapper(ItemViewer<T> wrapped) {
		super();
		this.mWrappedItem = wrapped;
	}

	@Override
	protected View onCreateView(ViewGroup parent, Context context) {
		return mWrappedItem.onCreateView(context, parent);
	}

	@Override
	protected boolean onBinding(T model, int index, SelectStatus status) {
		mWrappedItem.onBinding(mLayout, model, index);
		return false;
	}
}
