package com.andframe.layoutbind;

import com.andframe.activity.framework.AfView;
import com.andframe.adapter.AfListAdapter.IAfLayoutItem;
import com.andframe.feature.AfViewBinder;

public abstract class AfListItem<T> implements IAfLayoutItem<T>{
	
	private int layoutId;
	
	public AfListItem() {
		// TODO Auto-generated constructor stub
	}
	
	public AfListItem(int layoutId) {
		this.layoutId = layoutId;
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return layoutId;
	}
	
	@Override
	public void onHandle(AfView view) {
		// TODO Auto-generated method stub
		AfViewBinder binder = new AfViewBinder(this);
		binder.doBind(view.getView());
	}

}
