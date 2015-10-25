package com.andoffice.layoutbind;

import java.util.Date;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andoffice.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.AfLayoutModule;
import com.andframe.layoutbind.framework.IAfLayoutModule;
import com.andframe.view.AfMultiListView;
import com.andframe.view.pulltorefresh.AfPullToRefreshBase.OnRefreshListener;

public class ModuleListView extends AfLayoutModule
		implements IAfLayoutModule {

	public static final int LISTVIEW = 0;
	public static final int PRIGRESS = 1;
	public static final int NULLDATA = 2;

	protected ModuleNodata mNodata;
	protected FrameSelector mSelector;
	protected ModuleProgress mProgress;
	protected AfMultiListView mListView = null;

	public ModuleListView(AfPageable page) {
		super(page);
		mNodata = new ModuleNodata(page);
		mSelector = new FrameSelector(page);
		mProgress = new ModuleProgress(page);
		// 控件初始化
		if (isValid() && mSelector.isValid() && mSelector.isValid()
				&& mProgress.isValid()) {
			mListView = new AfMultiListView(page.findViewById(R.id.module_listview,ListView.class));
//			mListView.setPullFooterLayout(new PullRefreshFooter(page
//					.getContext()));
//			mListView.setPullHeaderLayout(new PullRefreshHeader(page
//					.getContext()));

//			Drawable divider = page.getResources().getDrawable(
//					R.drawable.line_horizontal);
//			mListView.getRefreshableView().setDividerHeight(1);
//			mListView.getRefreshableView().setDivider(divider);
		} else {
			mIsValid = false;
		}
	}

	@Override
	protected View findLayout(AfViewable view) {
		View layout = view.findViewById(R.id.module_listview);
		if (layout != null) {
			layout = (View) layout.getParent();
		}
		return layout;
	}

	public ModuleNodata getNoData() {
		return mNodata;
	}

	public final View getLayout() {
		return mLayout;
	}

	public final void hide() {
		if (mLayout.getVisibility() == View.VISIBLE) {
			mLayout.setVisibility(View.GONE);
		}
	}

	public final void show() {
		if (mLayout.getVisibility() != View.VISIBLE) {
			mLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean isValid() {
		return mIsValid;
	}

	public final void addMoreView() {
		if (mIsValid) {
			mListView.addMoreView();
		}
	}

	public final void removeMoreView() {
		if (mIsValid) {
			mListView.removeMoreView();
		}
	}

	public final int getHeaderViewsCount() {
		if (mIsValid) {
			return mListView.getHeaderViewsCount();
		}
		return 0;
	}

	public final int getIndex(int index) {
		if (mIsValid) {
			return mListView.getIndex(index);
		}
		return 0;
	}

	public final int getDataIndex(int index) {
		if (mIsValid) {
			return mListView.getDataIndex(index);
		}
		return 0;
	}

	public final Object getData(int positon) {
		if (mIsValid) {
			return mListView.getData(positon);
		}
		return null;
	}

	public final <E> E getData(int positon,Class<E> clazz) {
		if (mIsValid) {
			return mListView.getData(positon,clazz);
		}
		return null;
	}

	/**
	 * 提交刷新完成 更新时间
	 */
	public final void finishRefresh() {
		if (mIsValid) {
			mListView.finishRefresh();
		}
	}

	/**
	 * 提交刷新完成 但是失败 不更新时间
	 */
	public final void finishRefreshFail() {
		if (mIsValid) {
			mListView.finishRefreshFail();
		}
	}

	public final void finishLoadMore() {
		if (mIsValid) {
			mListView.finishLoadMore();
		}
	}

	public final void setLastUpdateTime(Date date) {
		if (mIsValid) {
			mListView.setLastUpdateTime(date);
		}
	}

	public final void setPullToRefreshEnabled(boolean enable) {
		if (mIsValid) {
			mListView.setPullToRefreshEnabled(enable);
		}
	}

	public final ListView getRefreshableView() {
		if (mIsValid) {
			return mListView.getRefreshableView();
		}
		return null;
	}

	public final AfMultiListView getAfRefreshableView() {
		if (mIsValid) {
			return mListView;
		}
		return null;
	}

	public final boolean isRefreshing() {
		if (mIsValid) {
			return mListView.isRefreshing();
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public final void setAdapter(ListAdapter adapter) {
		if (mIsValid) {
			mListView.setAdapter(adapter);
		}
	}

	public final void setOnRefreshListener(OnRefreshListener listener) {
		if (mIsValid) {
			mListView.setOnRefreshListener(listener);
		}
	}

	public final void setOnItemClickListener(OnItemClickListener listener) {
		if (mIsValid) {
			mListView.setOnItemClickListener(listener);
		}
	}

	public final void setOnItemLongClickListener(
			OnItemLongClickListener listener) {
		if (mIsValid) {
			mListView.setOnItemLongClickListener(listener);
		}
	}

	public final void setOnNodataRefreshListener(OnClickListener listener) {
		mNodata.setOnRefreshListener(listener);
	}

	public void setNoDataText(String description) {
		mNodata.setDescription(description);
	}

	public void setNoDataText(int id) {
		mNodata.setDescription(id);
	}

	public void setNoDataButtonText(String text) {
		mNodata.setButtonText(text);
	}

	public void setNoDataButtonText(int id) {
		mNodata.setButtonText(id);
	}

	public final boolean SelectFrame(int status) {
		switch (status) {
		case LISTVIEW:
			return mSelector.SelectFrame(mListView);
		case PRIGRESS:
			return mSelector.SelectFrame(mProgress.getLayout());
		case NULLDATA:
			return mSelector.SelectFrame(mNodata.getLayout());
		}
		return false;
	}

	@Override
	public void setEnabled(boolean enabled) {

	}
}
