package com.andframe.layoutbind;

import android.view.View.OnClickListener;

import com.andframe.activity.framework.AfView;
import com.andframe.adapter.AfListAdapter.IAfLayoutItem;
import com.andframe.feature.AfViewBinder;

public abstract class AfLisItem<T> implements IAfLayoutItem<T>, OnClickListener{

	@Override
	public void onHandle(AfView view) {
		// TODO Auto-generated method stub
		AfViewBinder binder = new AfViewBinder(this);
		binder.doBind(view.getView());
	}

}
