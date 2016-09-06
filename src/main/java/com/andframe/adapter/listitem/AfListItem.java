package com.andframe.adapter.listitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.annotation.view.BindLayout;
import com.andframe.api.ListItem;
import com.andframe.api.ViewQuery;
import com.andframe.application.AfApp;
import com.andframe.util.java.AfReflecter;

/**
 * 通用列表ITEM
 * @param <T>
 */
public abstract class AfListItem<T> implements ListItem<T> {
	
	private int layoutId;

	protected View mLayout;
	
	public AfListItem() {
	}
	
	public AfListItem(int layoutId) {
		this.layoutId = layoutId;
	}

	/**
	 * 获取 Item 关联的 InjectLayout ID
	 */
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
	
//	@Override
	protected void onHandle(View view) {
		Injecter.doInject(this, view.getContext());
		ViewBinder.doBind(this, view);
	}

	public View getLayout() {
		return mLayout;
	}

	@Override
	public View onCreateView(Context context, ViewGroup parent) {
		mLayout = LayoutInflater.from(context).inflate(getLayoutId(), parent, false);
		onHandle(mLayout);
		return mLayout;
	}

	/**
	 * 开始 ViewQuery 查询
	 * @param id 控件Id
	 */
	@SuppressWarnings("unused")
	protected ViewQuery $(int... id) {
		ViewQuery query = AfApp.get().newViewQuery(getLayout());
		if (id == null || id.length == 0) {
			return query;
		}
		return query.id(id);
	}
	@SuppressWarnings("unused")
	protected ViewQuery $(View view, View... views) {
		return AfApp.get().newViewQuery(getLayout()).id(view,views);
	}

}
