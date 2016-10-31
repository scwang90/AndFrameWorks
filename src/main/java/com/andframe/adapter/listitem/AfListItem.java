package com.andframe.adapter.listitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.annotation.pager.BindLayout;
import com.andframe.api.adapter.ListItem;
import com.andframe.api.view.ViewQuery;
import com.andframe.api.view.ViewQueryHelper;
import com.andframe.api.view.Viewer;
import com.andframe.feature.AfView;
import com.andframe.impl.helper.AfViewQueryHelper;
import com.andframe.util.java.AfReflecter;

/**
 * 通用列表ITEM
 * @param <T>
 */
public abstract class AfListItem<T> implements ListItem<T>, Viewer, ViewQueryHelper {
	
	private int layoutId;

	protected T mModel;
	protected int mIndex;
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
	protected void onViewCreated(View view) {
		Injecter.doInject(this, view.getContext());
		ViewBinder.doBind(this);
	}

	public View getLayout() {
		return mLayout;
	}

	@Override
	public View onCreateView(Context context, ViewGroup parent) {
		mLayout = LayoutInflater.from(context).inflate(getLayoutId(), parent, false);
		onViewCreated(mLayout);
		return mLayout;
	}

	@Override
	public void onBinding(View view, T model, int index) {
		mLayout = view;
		mIndex = index;
		mModel = model;
		onBinding(model,index);
	}

	public abstract void onBinding(T model, int index);

	//<editor-fold desc="ViewQuery 集成">
	ViewQueryHelper mViewQueryHelper = new AfViewQueryHelper(this);

	/**
	 * 开始 ViewQuery 查询
	 *
	 * @param id 控件Id
	 */
	@Override
	public ViewQuery<? extends ViewQuery> $(int id, int... ids) {
		return mViewQueryHelper.$(id, ids);
	}

	/**
	 * 开始 ViewQuery 查询
	 *
	 * @param views 可选的多个 View
	 */
	@Override
	public ViewQuery<? extends ViewQuery> $(View... views) {
		return mViewQueryHelper.$(views);
	}
	//</editor-fold>

	//<editor-fold desc="Viewr 接口实现">
	@Override
	public Context getContext() {
		return mLayout == null ? null : mLayout.getContext();
	}

	@Override
	public View getView() {
		return mLayout;
	}

	@Override
	public View findViewById(int id) {
		return mLayout == null ? null : mLayout.findViewById(id);
	}

	@Override
	public <TT extends View> TT findViewByID(int id) {
		return mLayout == null ? null : new AfView(mLayout).findViewByID(id);
	}

	@Override
	public <TT extends View> TT findViewById(int id, Class<TT> clazz) {
		return mLayout == null ? null : new AfView(mLayout).findViewById(id, clazz);
	}
	//</editor-fold>
}