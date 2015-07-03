package com.andoffice.layoutbind.framework;

import com.andframe.adapter.AfExpandableAdapter.IAfChildItem;
import com.andframe.adapter.AfExpandableAdapter.IAfGroupItem;

public abstract class AbComListItem<T> extends AbListItem<T> implements IAfChildItem<T>,IAfGroupItem<T>{

	@Override
	public void onBinding(T model, boolean isExpanded) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean onBinding(T model,int index, SelectStatus status) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
