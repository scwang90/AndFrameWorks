package com.andoffice.fragment.framework;

import java.util.List;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;

import com.andframe.activity.framework.AfView;
import com.andframe.adapter.AfListAdapter;
import com.andframe.application.AfApplication;
import com.andframe.feature.AfBundle;
import com.andframe.fragment.AfTabFragment;
import com.andframe.model.framework.AfModel;
import com.andframe.thread.AfListTask;
import com.andframe.thread.AfListViewTask;
import com.andframe.thread.AfTask;
import com.andframe.view.multichoice.AfMultiChoiceListView;
import com.andframe.view.pulltorefresh.AfPullToRefreshBase.OnRefreshListener;
import com.andoffice.R;
import com.andoffice.layoutbind.FrameSelector;
import com.andoffice.layoutbind.ModuleBottombar;
import com.andoffice.layoutbind.ModuleBottombarSelector;
import com.andoffice.layoutbind.ModuleListView;
import com.andoffice.layoutbind.ModuleNodata;
import com.andoffice.layoutbind.ModuleTitleMain;
import com.andoffice.layoutbind.ModuleTitlebar;
import com.andoffice.layoutbind.ModuleTitlebarSearcher;
import com.andoffice.layoutbind.ModuleTitlebarSelector;

public abstract class AbListViewFragment<T extends AfModel> extends AfTabFragment implements
		OnClickListener, OnItemClickListener, Callback, OnRefreshListener,
		OnItemLongClickListener {

	protected FrameLayout mFrameTop = null;
	protected FrameLayout mFrameBot = null;
	protected FrameSelector mFrameSelector;
	protected ModuleTitlebar mTitlebar;
	protected ModuleTitleMain mTitleMain;
	protected ModuleBottombar mBottombar;
	protected ModuleTitlebarSelector mTitlebarSelector = null;
	protected ModuleTitlebarSearcher mTitlebarSearcher = null;
	protected ModuleBottombarSelector mBottombarSelector = null;
	protected ModuleListView mModuleListView = null;

	// 标识是否分页
	protected boolean mIsPaging = false;

	protected List<T> mltArray = null;
	protected AfListAdapter<T> mAdapter = null;

	// 正在加载数据标识
	protected boolean mIsLoading = false;

	private Handler mHandler = null;
	
	private Handler getHandler() {
		// TODO Auto-generated method stub
		if(mHandler == null){
			mHandler = new Handler(AfApplication.getLooper(), this);
		}
		return mHandler;
	}


	/**
	 * 当服务器加载数据为空的时候调用 处理信息提示 如果不重写这个函数 默认的提示是 抱歉，附近无相关数据
	 */
	protected void onNodata(ModuleNodata nodata) {
		// 设置空数据页面
		nodata.setDescription(ModuleNodata.TEXT_NODATA);
		nodata.setButtonText(ModuleNodata.TEXT_TOREFRESH);
	}

	protected abstract AfListViewTask<T> getTask(Handler handler,int task);

	protected abstract AfListAdapter<T> getAdapter(List<T> ltdata);

	@Override
	protected final View onCreateView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.layout_listview, container, false);
	}
	
	@Override
	protected final void onCreated(AfBundle bundle,AfView view) throws Exception {
		// TODO Auto-generated method stub
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
		
		// 执行子类创建
		onCreate(bundle,mModuleListView.getAfRefreshableView());
	}

	/**
	 * 为了防止子类直接实现 Activity.onCreate 把它设置final 为了子类能得到onCreate 消息通知 在
	 * Activity.onCreate 中调用 onCreate 子类可以在 onCreate 中执行其它特殊的初始化操作 如修改标题
	 */
	protected void onCreate(AfBundle bundle,AfMultiChoiceListView listview) throws Exception {
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
		if (mltArray == null) {
			mModuleListView.SelectFrame(ModuleListView.PRIGRESS);
		} else if (mltArray.size() == 0) {
			if (mIsLoading == true) {
				mModuleListView.SelectFrame(ModuleListView.PRIGRESS);
			} else {
				this.onNodata(mModuleListView.getNoData());
				mModuleListView.SelectFrame(ModuleListView.NULLDATA);
			}
		} else {
			mModuleListView.setAdapter(mAdapter);
			mModuleListView.SelectFrame(ModuleListView.LISTVIEW);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> absview, View view, int index,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> absview, View view,
			int index, long id) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 第一次切换到本页面
	 */
	@Override
	protected final void onFirstSwitchOver() {
		// TODO Auto-generated method stub
		postTask(getTask(getHandler(),AfTask.TASK_LOAD));
	}

	/**
	 * 发送刷新任务
	 * @param progress 是否显示正在加载页面
	 */
	protected void postRefreshTask(boolean progress) {
		// TODO Auto-generated method stub
		postTask(getTask(getHandler(),AfListTask.TASK_REFRESH));
		if(progress){
			mModuleListView.SelectFrame(ModuleListView.PRIGRESS);
		}
	}
	
	@Override
	public final boolean onMore() {
		// TODO Auto-generated method stub
		// 抛送更多任务
		AfListViewTask<T> task = getTask(getHandler(),AfListTask.TASK_MORE);
		task.mFirstResult = mltArray.size();
		postTask(task);
		return true;
	}

	@Override
	public final boolean onRefresh() {
		// TODO Auto-generated method stub
		// 抛送刷新任务
		postTask(getTask(getHandler(),AfListTask.TASK_REFRESH));
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == ModuleNodata.ID_BUTTON
				|| v.getId() == ModuleNodata.TEXT_TOREFRESH) {
			// 设置正在加载页面
			mModuleListView.SelectFrame(ModuleListView.PRIGRESS);
			// 抛送刷新任务
			postTask(getTask(getHandler(),AfListTask.TASK_REFRESH));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		AfListViewTask<T> tTask = (AfListViewTask<T>) msg.obj;
		if (tTask.mTask == AfTask.TASK_LOAD) {
			if (tTask.mltData.size() > 0) {
				// 设置ListView页面
				mltArray = tTask.mltData;
				mAdapter = getAdapter(mltArray);
				mModuleListView.setAdapter(mAdapter);
				mModuleListView.SelectFrame(ModuleListView.LISTVIEW);
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
				mltArray = tTask.mltData;
				if(mAdapter != null && mAdapter.getCount() > 0){
					mAdapter.setData(mltArray);
				}else{
					mAdapter = getAdapter(mltArray);
					mModuleListView.setAdapter(mAdapter);
				}
				if (tTask.mltData.size() > 0) {
					// 恢复列表页面
					mModuleListView.SelectFrame(ModuleListView.LISTVIEW);
					// 如果开启分页
					if (mIsPaging
							&& tTask.mltData.size() >= AfListViewTask.PAGE_SIZE) {
						mModuleListView.addMoreView();
					}
				}
				// 无数据设置空页面
				else if (mltArray == null || mltArray.size() == 0) {
					this.onNodata(mModuleListView.getNoData());
					mModuleListView.SelectFrame(ModuleListView.NULLDATA);
				}
				break;
			case AfListTask.TASK_MORE:
				if (tTask.mltData.size() > 0) {
					// 通知列表刷新完成
					mModuleListView.finishLoadMore();
					// 更新列表
					mAdapter.addData(tTask.mltData);
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
			if (mltArray == null || mltArray.size() == 0) {
				// 设置空数据页面
				mModuleListView.setNoDataText(tTask.mErrors);
				mModuleListView.setNoDataButtonText(ModuleNodata.TEXT_TOREFRESH);
				mModuleListView.SelectFrame(ModuleListView.NULLDATA);
			} else {
				// 恢复列表页面
				mModuleListView.SelectFrame(ModuleListView.LISTVIEW);
			}
		}
		return tTask.mTask == AfListTask.TASK_REFRESH
				|| tTask.mTask == AfListTask.TASK_MORE;
	}

}
