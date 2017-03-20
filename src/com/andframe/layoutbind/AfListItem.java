package com.andframe.layoutbind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.activity.framework.AfView;
import com.andframe.activity.framework.AfViewable;
import com.andframe.activity.framework.IViewQuery;
import com.andframe.adapter.AfListAdapter.IListItem;
import com.andframe.annotation.interpreter.Injecter;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.annotation.interpreter.ViewBinder;
import com.andframe.helper.android.AfViewQueryHelper;

/**
 * 通用列表ITEM
 * @param <T>
 */
public abstract class AfListItem<T> implements IListItem<T> , AfViewable {
	
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


	//<editor-fold desc="IViewQuery 集成">
	IViewQuery<? extends IViewQuery> $$ = AfViewQueryHelper.newHelper(this);

	public IViewQuery<? extends IViewQuery> $(View... views) {
		return $$.$(views);
	}

	public IViewQuery<? extends IViewQuery> $(Integer id, int... ids) {
		return $$.$(id, ids);
	}

	public IViewQuery<? extends IViewQuery> $(String idvalue, String... idvalues) {
		return $$.$(idvalue, idvalues);
	}

	public IViewQuery<? extends IViewQuery> $(Class<? extends View> type, Class<? extends View>... types) {
		return $$.$(type, types);
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
