package com.andframe.layoutbind;

import android.view.View;

import com.andframe.activity.framework.AfView;
import com.andframe.adapter.AfListAdapter.IAfLayoutItem;
import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.annotation.view.BindLayout;
import com.andframe.util.java.AfReflecter;

public abstract class AfListItem<T> implements IAfLayoutItem<T>{

	private int layoutId;

	protected View mLayout;

	public AfListItem() {
	}

	public AfListItem(int layoutId) {
		this.layoutId = layoutId;
	}

	@Override
	public int getLayoutId() {
		if (layoutId != 0) {
			return layoutId;
		}
		BindLayout layout = AfReflecter.getAnnotation(this.getClass(), AfListItem.class, BindLayout.class);
		if (layout != null) {
			return layout.value();
		}
		return layoutId;
	}

	@Override
	public void onHandle(AfView view) {
		ViewBinder.doBind(this, mLayout = view.getView());
		Injecter.doInject(this, view.getContext());
	}

	public View getLayout() {
		return mLayout;
	}
}
