package com.andoffice.layoutbind;

import java.util.Date;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.layoutbind.framework.AfViewModule;
import com.andoffice.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;
import com.andframe.view.AfMultiListView;
import com.andframe.view.pulltorefresh.AfPullToRefreshBase.OnRefreshListener;

public class ModuleListView extends AfViewModule{

	public static final int LISTVIEW = 0;
	public static final int PRIGRESS = 1;
	public static final int NULLDATA = 2;

	protected ModuleNodata mNodata;
	protected FrameSelector mSelector;
	protected ModuleProgress mProgress;
	protected AfMultiListView mListView = null;

	public ModuleListView(AfPageable page) {
		super(page,R.id.module_listview_frame);
		mNodata = new ModuleNodata(page);
		mSelector = new FrameSelector(page);
		mProgress = new ModuleProgress(page);
		// 控件初始化
		if (isValid() && mSelector.isValid() && mSelector.isValid()
				&& mProgress.isValid()) {
			mListView = new AfMultiListView(page.findViewById(R.id.module_listview,ListView.class));
		}
	}

	public ModuleNodata getNoData() {
		return mNodata;
	}

	public final void addMoreView() {
		if (isValid()) {
			mListView.addMoreView();
		}
	}

	public final void removeMoreView() {
		if (isValid()) {
			mListView.removeMoreView();
		}
	}

	public final int getHeaderViewsCount() {
		if (isValid()) {
			return mListView.getHeaderViewsCount();
		}
		return 0;
	}

	public final int getIndex(int index) {
		if (isValid()) {
			return mListView.getIndex(index);
		}
		return 0;
	}

	public final int getDataIndex(int index) {
		if (isValid()) {
			return mListView.getDataIndex(index);
		}
		return 0;
	}

	public final Object getData(int positon) {
		if (isValid()) {
			return mListView.getData(positon);
		}
		return null;
	}

	public final <E> E getData(int positon,Class<E> clazz) {
		if (isValid()) {
			return mListView.getData(positon,clazz);
		}
		return null;
	}

	/**
	 * 提交刷新完成 更新时间
	 */
	public final void finishRefresh() {
		if (isValid()) {
			mListView.finishRefresh();
		}
	}

	/**
	 * 提交刷新完成 但是失败 不更新时间
	 */
	public final void finishRefreshFail() {
		if (isValid()) {
			mListView.finishRefreshFail();
		}
	}

	public final void finishLoadMore() {
		if (isValid()) {
			mListView.finishLoadMore();
		}
	}

	public final void setLastUpdateTime(Date date) {
		if (isValid()) {
			mListView.setLastUpdateTime(date);
		}
	}

	public final void setPullToRefreshEnabled(boolean enable) {
		if (isValid()) {
			mListView.setPullToRefreshEnabled(enable);
		}
	}

	public final ListView getRefreshableView() {
		if (isValid()) {
			return mListView.getRefreshableView();
		}
		return null;
	}

	public final AfMultiListView getAfRefreshableView() {
		if (isValid()) {
			return mListView;
		}
		return null;
	}

	public final boolean isRefreshing() {
		if (isValid()) {
			return mListView.isRefreshing();
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public final void setAdapter(ListAdapter adapter) {
		if (isValid()) {
			mListView.setAdapter(adapter);
		}
	}

	public final void setOnRefreshListener(OnRefreshListener listener) {
		if (isValid()) {
			mListView.setOnRefreshListener(listener);
		}
	}

	public final void setOnItemClickListener(OnItemClickListener listener) {
		if (isValid()) {
			mListView.setOnItemClickListener(listener);
		}
	}

	public final void setOnItemLongClickListener(
			OnItemLongClickListener listener) {
		if (isValid()) {
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

	public final boolean selectFrame(int status) {
		switch (status) {
		case LISTVIEW:
			return mSelector.selectFrame(mListView);
		case PRIGRESS:
			return mSelector.selectFrame(mProgress.getLayout());
		case NULLDATA:
			return mSelector.selectFrame(mNodata);
		}
		return false;
	}

	@Override
	public void setEnabled(boolean enabled) {

	}
}
