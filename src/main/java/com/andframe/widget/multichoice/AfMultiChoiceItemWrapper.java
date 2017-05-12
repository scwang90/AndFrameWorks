package com.andframe.widget.multichoice;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.adapter.ItemViewer;

/**
 * 多选ITEM模板
 * @param <T>
 */
@SuppressWarnings("unused")
public class AfMultiChoiceItemWrapper<T> extends AfMultiChoiceItemViewer<T> {

	protected ItemViewer<T> mWrappedItem;

	public AfMultiChoiceItemWrapper(ItemViewer<T> wrapped) {
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
