package com.andframe.adapter.itemviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.view.ViewQuery;
import com.andframe.api.view.ViewQueryHelper;
import com.andframe.api.view.Viewer;
import com.andframe.feature.AfView;
import com.andframe.fragment.AfFragment;
import com.andframe.impl.helper.AfViewQueryHelper;

/**
 * 通用列表ITEM
 * @param <T>
 */
public abstract class AfItemViewer<T> implements ItemViewer<T>, Viewer, ViewQueryHelper {
	
	private int layoutId;

	protected T mModel;
	protected int mIndex;
	protected View mLayout;

	public AfItemViewer() {
	}
	
	public AfItemViewer(int layoutId) {
		this.layoutId = layoutId;
	}

	/**
	 * 获取 Item 关联的 InjectLayout ID
	 */
	public int getLayoutId(Context context) {
		if (layoutId != 0) {
			return layoutId;
		}
		return LayoutBinder.getBindLayoutId(this, context, AfFragment.class);
	}
	
//	@Override
	public void onViewCreated(View view) {
		Injecter.doInject(this, view.getContext());
		ViewBinder.doBind(this);
	}

	public View getLayout() {
		return mLayout;
	}

	@Override
	public View onCreateView(Context context, ViewGroup parent) {
		mLayout = LayoutInflater.from(context).inflate(getLayoutId(context), parent, false);
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
	ViewQuery<? extends ViewQuery> $$ = AfViewQueryHelper.newHelper(this);

	@Override
	public ViewQuery<? extends ViewQuery> $(View... views) {
		return $$.$(views);
	}

	@Override
	public ViewQuery<? extends ViewQuery> $(Integer id, int... ids) {
		return $$.$(id, ids);
	}

	@Override
	public ViewQuery<? extends ViewQuery> $(String idvalue, String... idvalues) {
		return $$.$(idvalue);
	}

	@Override
	public ViewQuery<? extends ViewQuery> $(Class<? extends View> type) {
		return $$.$(type);
	}

	@Override
	public ViewQuery<? extends ViewQuery> $(Class<? extends View>[] types) {
		return $$.$(types);
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
