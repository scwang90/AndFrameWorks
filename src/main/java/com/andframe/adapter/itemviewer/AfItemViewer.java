package com.andframe.adapter.itemviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.annotation.view.BindViewCreated;
import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.api.viewer.ViewQueryHelper;
import com.andframe.api.viewer.Viewer;
import com.andframe.impl.helper.AfViewQueryHelper;
import com.andframe.impl.viewer.AfView;

import java.util.Collection;

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
		return LayoutBinder.getBindLayoutId(this, context);
	}
	
	@BindViewCreated
	public void onViewCreated() {
	}

	public View getLayout() {
		return mLayout;
	}

	@Override
	public final View onCreateView(Context context, ViewGroup parent) {
		mLayout = onCreateView(parent, context);
		Injecter.doInject(this, context);
		ViewBinder.doBind(this);
		return mLayout;
	}

	protected View onCreateView(ViewGroup parent, Context context) {
		return LayoutInflater.from(context).inflate(getLayoutId(context), parent, false);
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
	protected ViewQuery<? extends ViewQuery> $$ = AfViewQueryHelper.newHelper(this);

//	@Override
//	public void setViewQuery(ViewQuery<? extends ViewQuery> viewQuery) {
//		this.$$ = viewQuery;
//	}
//
//	@Override
//	public ViewQuery<? extends ViewQuery> getViewQuery() {
//		return $$;
//	}

	@Override
	public ViewQuery<? extends ViewQuery> $(View... views) {
		return $$.with(views);
	}

	@Override
	public ViewQuery<? extends ViewQuery> $(Collection<View> views) {
		return $$.with(views);
	}

	@Override
	public ViewQuery<? extends ViewQuery> $(Integer id, int... ids) {
		return $$.query(id, ids);
	}

	@Override
	public ViewQuery<? extends ViewQuery> $(String idValue, String... idValues) {
		return $$.query(idValue);
	}

	@Override
	public ViewQuery<? extends ViewQuery> $(Class<? extends View> type) {
		return $$.query(type);
	}

	@Override
	public ViewQuery<? extends ViewQuery> $(Class<? extends View>[] types) {
		return $$.query(types);
	}

	@Override
	public ViewQuery<? extends ViewQuery> with(View... views) {
		return $$.with(views);
	}

	@Override
	public ViewQuery<? extends ViewQuery> with(Collection<View> views) {
		return $$.with(views);
	}

	@Override
	public ViewQuery<? extends ViewQuery> query(Integer id, int... ids) {
		return $$.query(id, ids);
	}

	@Override
	public ViewQuery<? extends ViewQuery> query(String idValue, String... idValues) {
		return $$.query(idValue);
	}

	@Override
	public ViewQuery<? extends ViewQuery> query(Class<? extends View> type) {
		return $$.query(type);
	}

	@Override
	public ViewQuery<? extends ViewQuery> query(Class<? extends View>[] types) {
		return $$.query(types);
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
	public <TT extends View> TT  findViewById(int id) {
		return mLayout == null ? null : mLayout.findViewById(id);
	}

	@Override
	public <TT extends View> TT findViewById(int id, Class<TT> clazz) {
		return mLayout == null ? null : new AfView(mLayout).findViewById(id, clazz);
	}
	//</editor-fold>
}
