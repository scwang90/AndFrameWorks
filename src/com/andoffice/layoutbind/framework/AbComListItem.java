package com.andoffice.layoutbind.framework;

//import com.andframe.adapter.AfExpandableAdapter.IAfChildItem;
public abstract class AbComListItem<T> extends AbListItem<T> /*implements IAfChildItem<T>,IAfGroupItem<T>*/{

//	@Override
//	public void onBinding(T model, boolean isExpanded) {
//
//	}
//	@Override
//	public void onBinding(T model, boolean isExpanded) {
//
//	}

	@Override
	protected boolean onBinding(T model,int index, SelectStatus status) {
		return false;
	}
	
}
