package com.andoffice.activity.framework;

import java.util.List;

import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.andframe.activity.AfActivity;
import com.andframe.adapter.AfListAdapter;
import com.andframe.feature.AfIntent;
import com.andframe.thread.AfListTask;
import com.andframe.thread.AfListViewTask;
import com.andframe.thread.AfTask;
import com.andframe.view.AfRefreshListView;
import com.andframe.view.multichoice.AfMultiChoiceListView;
import com.andframe.view.pulltorefresh.AfPullToRefreshBase.OnRefreshListener;
import com.andoffice.R;
import com.andoffice.layoutbind.ModuleBottombar;
import com.andoffice.layoutbind.ModuleBottombarSelector;
import com.andoffice.layoutbind.ModuleListView;
import com.andoffice.layoutbind.ModuleNodata;
import com.andoffice.layoutbind.ModuleTitleMain;
import com.andoffice.layoutbind.ModuleTitlebar;
import com.andoffice.layoutbind.ModuleTitlebarSearcher;
import com.andoffice.layoutbind.ModuleTitlebarSelector;

public abstract class AbListViewActivity<T> extends AfActivity implements
		OnClickListener, OnItemClickListener, Callback, OnRefreshListener,
		OnItemLongClickListener {

	protected FrameLayout mFrameTop = null;
	protected FrameLayout mFrameBot = null;
	protected ModuleTitlebar mTitlebar = null;
	protected ModuleTitleMain mTitleMain = null;
	protected ModuleBottombar mBottombar = null;
	protected ModuleTitlebarSelector mTitlebarSelector = null;
	protected ModuleTitlebarSearcher mTitlebarSearcher = null;
	protected ModuleBottombarSelector mBottombarSelector = null;
	protected ModuleListView mModuleListView = null;

	// 标识是否分页
	protected boolean mIsPaging = true;

//	protected List<T> mltArray = new ArrayList<T>();
	protected AfListAdapter<T> mAdapter = null;

	protected abstract AfListViewTask<T> getTask(int task);

	protected abstract AfListAdapter<T> getAdapter(List<T> ltdata);

	/**
	 * 为了防止子类直接实现 Activity.onCreate 把它设置final 为了子类能得到onCreate 消息通知 在
	 * Activity.onCreate 中调用 onCreate 子类可以在 onCreate 中执行其它特殊的初始化操作 如修改标题
	 * @throws Exception 
	 */
	@Override
	protected final void onCreate(Bundle bundle,AfIntent intent) throws Exception {
		super.onCreate(bundle, intent);
		setContentView(R.layout.layout_listview);
		// 控件初始化
		mTitlebar = new ModuleTitlebar(this);
		mTitleMain = new ModuleTitleMain(this);
		mBottombar = new ModuleBottombar(this);
		mTitlebarSelector = new ModuleTitlebarSelector(this);
		mTitlebarSearcher = new ModuleTitlebarSearcher(this);
		mBottombarSelector = new ModuleBottombarSelector(this);
		mModuleListView = new ModuleListView(this);
		
		//获取布局
		mFrameTop = findViewById(R.id.listview_topframe, FrameLayout.class);
		mFrameBot = findViewById(R.id.listview_botframe, FrameLayout.class);
		
		onCreate(bundle,intent,mModuleListView.getAfRefreshableView());
		// 抛送加载任务
		postTask(getTask(AfListTask.TASK_REFRESH));	
		
	}

	protected void onCreate(Bundle bundle,AfIntent intent,AfRefreshListView<ListView> listview) throws Exception {
		//绑定 Selector 到 ListView
		if(listview instanceof AfMultiChoiceListView){
			AfMultiChoiceListView mclistview = (AfMultiChoiceListView)listview;
			mclistview.setSelector(mTitlebarSelector);
			mclistview.setSelector(mBottombarSelector);
		}
		// 事件绑定
		mModuleListView.setOnItemClickListener(this);
		mModuleListView.setOnRefreshListener(this);
		mModuleListView.setOnItemLongClickListener(this);
		mModuleListView.setOnNodataRefreshListener(this);

		// 设置正在加载页面
		mModuleListView.selectFrame(ModuleListView.PRIGRESS);
	}

	/**
	 * 当服务器加载数据为空的时候调用 处理信息提示 如果不重写这个函数 默认的提示是 抱歉，附近无相关数据
	 * @param nodata
	 */
	protected void onNodata(ModuleNodata nodata) {
		// 设置空数据页面
		nodata.setDescription(ModuleNodata.TEXT_NODATA);
		nodata.setButtonText(ModuleNodata.TEXT_TOREFRESH);
	}
	
	/**
	 * 发送刷新任务
	 * @param progress 是否显示正在加载页面
	 */
	protected void postRefreshTask(boolean progress) {
		postTask(getTask(AfListTask.TASK_REFRESH));
		if(progress){
			mModuleListView.selectFrame(ModuleListView.PRIGRESS);
		}
	}

	protected boolean doMoveTo(View view, FrameLayout frame,int index) {
		ViewParent parent = view.getParent();
		if(parent != null){
			if(parent instanceof ViewGroup){
				ViewGroup group = (ViewGroup)parent;
				group.removeView(view);
				frame.addView(view,index);
				return true;
			}
			return false;
		}else{
			frame.addView(view,index);
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> absview, View view,long id, int index) {
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> absview, View view,
			int index, long id) {
		return false;
	}

	@Override
	public boolean onMore() {
		// 抛送更多任务
		AfListViewTask<T> task = getTask(AfListTask.TASK_MORE);
		task.mFirstResult = mAdapter.getCount();
		postTask(task);
		return true;
	}

	@Override
	public boolean onRefresh() {
		// 抛送刷新任务
		postTask(getTask(AfListTask.TASK_REFRESH));
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == null) {
			return;
		}
		if (v.getId() == ModuleNodata.ID_BUTTON
				|| v.getId() == ModuleNodata.TEXT_TOREFRESH) {
			// 设置正在加载页面
			mModuleListView.selectFrame(ModuleListView.PRIGRESS);
			// 抛送刷新任务
			postTask(getTask(AfListTask.TASK_REFRESH));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean handleMessage(Message msg) {
		AfListViewTask<T> tTask = (AfListViewTask<T>) msg.obj;
		if (tTask.mTask == AfTask.TASK_LOAD) {
			if (tTask.mltData.size() > 0) {
				// 设置ListView页面
				mAdapter = getAdapter(tTask.mltData);
				mModuleListView.setAdapter(mAdapter);
				mModuleListView.selectFrame(ModuleListView.LISTVIEW);
			} else {
				// 本地加载为空 执行网络刷新
				tTask.mTask = AfListTask.TASK_REFRESH;
				postTask(tTask);
			}
			return true;
		} else if (tTask.mResult == AfTask.RESULT_FINISH) {
			switch (tTask.mTask) {
			case AfListTask.TASK_REFRESH:
				// 如果是列表刷新 通知列表刷新完成
				if (mModuleListView.isRefreshing()) {
					mModuleListView.finishRefresh();
				}
				// 有数据更新列表
				if(mAdapter != null && mAdapter.getCount() > 0){
					mAdapter.setData(tTask.mltData);
				}else{
					mAdapter = getAdapter(tTask.mltData);
					mModuleListView.setAdapter(mAdapter);
				}
				if (tTask.mltData.size() > 0) {
					// 恢复列表页面
					mModuleListView.selectFrame(ModuleListView.LISTVIEW);
					// 如果开启分页
					if (mIsPaging
							&& tTask.mltData.size() >= AfListViewTask.PAGE_SIZE) {
						mModuleListView.addMoreView();
					}
				}
				// 无数据设置空页面
				else if (tTask.mltData == null || tTask.mltData.size() == 0) {
					this.onNodata(mModuleListView.getNoData());
					mModuleListView.selectFrame(ModuleListView.NULLDATA);
				}
				break;
			case AfListTask.TASK_MORE:
				if (tTask.mltData.size() > 0) {
					int count = mAdapter.getCount();
					// 通知列表刷新完成
					mModuleListView.finishLoadMore();
					// 更新列表
					mAdapter.addData(tTask.mltData);
					mModuleListView.getAfRefreshableView().getRefreshableView().smoothScrollToPosition(count);
				}
				if (tTask.mltData.size() < AfListViewTask.PAGE_SIZE) {
					// 关闭更多选项
					mModuleListView.removeMoreView();
					makeToastShort("数据全部加载完毕！");
				}
				break;
			}
		} else if (tTask.mResult == AfTask.RESULT_FAIL) {
			if (mModuleListView.isRefreshing()) {
				mModuleListView.finishRefreshFail();
			}
			if (tTask.mltData == null || tTask.mltData.size() == 0) {
				// 设置空数据页面
				mModuleListView.setNoDataText(tTask.mErrors);
				mModuleListView.setNoDataButtonText(ModuleNodata.TEXT_TOREFRESH);
				mModuleListView.selectFrame(ModuleListView.NULLDATA);
			} else {
				// 恢复列表页面
				mModuleListView.selectFrame(ModuleListView.LISTVIEW);
			}
		}
		return tTask.mTask == AfListTask.TASK_REFRESH
				|| tTask.mTask == AfListTask.TASK_MORE;
	}

}
