package com.andframe.layoutbind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.activity.framework.AfView;
import com.andframe.activity.framework.IViewQuery;
import com.andframe.adapter.AfListAdapter.IListItem;
import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.application.AfApp;

/**
 * 通用列表ITEM
 * @param <T>
 */
public abstract class AfListItem<T> implements IListItem<T> {
	
	private int layoutId;

	protected View mLayout;
	
	public AfListItem() {
	}
	
	public AfListItem(int layoutId) {
		this.layoutId = layoutId;
	}

	/**
	 * 获取 Item 关联的 InjectLayout ID
	 * @param context
	 */
	public int getLayoutId(Context context) {
		if (layoutId > 0) {
			return layoutId;
		}
		return LayoutBinder.getBindLayoutId(this, context, AfListItem.class);
	}
	
	@Override
	public void onHandle(AfView view) {
		Injecter.doInject(this, view.getContext());
		ViewBinder.doBind(this, mLayout = view.getView());
	}

	public View getLayout() {
		return mLayout;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
		return mLayout = inflater.inflate(getLayoutId(inflater.getContext()), parent, false);
	}

	/**
	 * 开始 IViewQuery 查询
	 * @param id 控件Id
	 */
	@SuppressWarnings("unused")
	protected IViewQuery $(int... id) {
		IViewQuery query = AfApp.get().getViewQuery(mLayout);
		if (id == null || id.length == 0) {
			return query;
		}
		return query.$(id[0]);
	}
	@SuppressWarnings("unused")
	protected IViewQuery $(View view) {
		return AfApp.get().getViewQuery(view);
	}

}
