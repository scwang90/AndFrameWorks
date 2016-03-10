package com.andoffice;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.andframe.activity.framework.AfActivity;
import com.andframe.activity.framework.AfPageable;
import com.andframe.adapter.AfListAdapter;
import com.andframe.adapter.AfListAdapter.IAfLayoutItem;
import com.andframe.bean.Page;
import com.andframe.exception.AfException;
import com.andframe.feature.AfIntent;
import com.andframe.layoutbind.AfFrameSelector;
import com.andframe.layoutbind.AfModuleNodata;
import com.andframe.layoutbind.AfModuleProgress;
import com.andframe.thread.AfListViewTask;
import com.andframe.util.java.AfCollections;
import com.andframe.view.AfListView;
import com.andframe.view.pulltorefresh.AfPullToRefreshBase.OnRefreshListener;
/**
 * 数据列表框架 Activity
 * @author 树朾
 * @param <T> 列表数据实体类
 */
public abstract class AoListViewActivity<T> extends AfActivity implements OnRefreshListener, OnItemClickListener, OnClickListener{

	protected static final String EXTRA_LAYOUT = "EXTRA_LAYOUT";
	
	protected AfModuleNodata mNodata;
	protected AfModuleProgress mProgress;
	protected AfFrameSelector mSelector;

	protected AfListView mListView;
	protected AfListAdapter<T> mAdapter;
	
	protected boolean mPagingEnable = true;

	/**
	 * 缓存使用的 class 对象（json要用到）
	 * 设置 并且任务为 TASK_LOAD AfListTask 将自动使用缓存功能
	 */
	public Class<T> mCacheClazz = null;
	/**
	 * 缓存使用的 KEY_CACHELIST = this.getClass().getName() KEY_CACHELIST 为缓存的标识
	 */
	public String KEY_CACHELIST = this.getClass().getName();
	
	public AoListViewActivity() {

	}

	/**
	 * 使用缓存必须调用这个构造函数
	 * @param clazz
	 */
	public AoListViewActivity(Class<T> clazz) {
		this.mCacheClazz = clazz;
	}

	/**
	 * 使用缓存必须调用这个构造函数
	 * 	可以自定义缓存标识
	 * @param clazz
	 */
	public AoListViewActivity(Class<T> clazz, String KEY_CACHELIST) {
		this.mCacheClazz = clazz;
		this.KEY_CACHELIST = KEY_CACHELIST;
	}

	/**
	 * 创建方法
	 * @author 树朾
	 * @Modified:
	 * @param bundle
	 * @param intent
	 * @throws Exception
	 */
	protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
		super.onCreate(bundle, intent);
		setContentView(getLayoutId());

		mNodata = newModuleNodata(this);
		mProgress = newModuleProgress(this);
		mSelector = newAfFrameSelector(this);

		mListView = new AfListView(findListView(this));
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		
		// 设置banner尺寸
		setLoading();
		postTask(new AbListViewTask(mCacheClazz,KEY_CACHELIST));
	}

	/**
	 * 获取setContentView的id
	 * @author 树朾
	 * @return id
	 */
	protected abstract int getLayoutId();
	/**
	 * 获取列表控件
	 * @param pageable
	 * @return pageable.findListViewById(id)
	 */
	protected abstract ListView findListView(AfPageable pageable);

	/**
	 * 新建页面选择器
	 * @param pageable
	 * @return
	 */
	protected abstract AfFrameSelector newAfFrameSelector(AfPageable pageable);
	/**
	 * 新建加载页面
	 * @param pageable
	 * @return
	 */
	protected abstract AfModuleProgress newModuleProgress(AfPageable pageable);
	/**
	 * 新建空数据页面
	 * @param pageable
	 * @return
	 */
	protected abstract AfModuleNodata newModuleNodata(AfPageable pageable);


	/**
	 * 显示数据页面
	 * @author 树朾
	 * @param adapter
	 */
	public void setData(AfListAdapter<T> adapter) {
		mListView.setAdapter(adapter);
		mSelector.selectFrame(mListView);
	}

	/**
	 * 正在加载数据提示
	 * @author 树朾
	 */
	public void setLoading() {
		mProgress.setDescription("正在加载...");
		mSelector.selectFrame(mProgress);
	}
	
	/**
	 * 空数据页面刷新监听器
	 * 子类需要重写监听器的话可以对 
	 * mNodataRefreshListener 重新赋值
	 */
	private OnClickListener mNodataRefreshListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			onRefresh();
			setLoading();
		}
	};

	/**
	 * 处理空数据
	 * @author 树朾
	 */
	public void setNodata() {
		mNodata.setDescription("抱歉，暂无数据");
		mSelector.selectFrame(mNodata);
		mNodata.setOnRefreshListener(mNodataRefreshListener);
	}

	/**
	 * 错误信息处理
	 * @author 树朾
	 * @param ex
	 */
	public void setLoadError(Throwable ex) {
		mNodata.setDescription(AfException.handle(ex, "数据加载出现异常"));
		mNodata.setOnRefreshListener(mNodataRefreshListener);
		mSelector.selectFrame(mNodata);
	}

	/**
	 * 用户加载分页通知事件
	 * @author 树朾
	 * @return
	 */
	@Override
	public boolean onMore() {
		postTask(new AbListViewTask(mAdapter));
		return true;
	}

	/**
	 * 用户刷新数据通知事件
	 * @author 树朾
	 * @return
	 */
	@Override
	public boolean onRefresh() {
		postTask(new AbListViewTask(null));
		return true;
	}

	/**
	 * 数据列表点击事件
	 * @author 树朾
	 * @param parent
	 * @param view
	 * @param index
	 * @param id
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
		T model = mAdapter.getItemAt(mListView.getDataIndex(index));
		onItemClick(model,index);
	}

	/**
	 * onItemClick 事件的 包装 一般情况下子类可以重写这个方法
	 * @author 树朾
	 * @param model
	 * @param index
	 */
	protected void onItemClick(T model, int index) {

	}

	@Override
	public void onClick(View v) {
//		if (v != null && v.getId() == ModuleNodata.ID_BUTTON) {
//			onRefresh();
//			setLoading();
//		}
	}

	/**
	 * 数据加载内部任务类（数据加载事件已经转发，无实际处理代码）
	 * @author 树朾
	 */
	protected class AbListViewTask extends AfListViewTask<T> {

		/**
		 * 可以触发加载缓存任务（框架缓存加载失败会改变成刷新任务）
		 * @param clazz
		 * @param KEY_CACHELIST
		 */
		public AbListViewTask(Class<T> clazz, String KEY_CACHELIST) {
			super(clazz, KEY_CACHELIST);
		}

		/**
		 * 可以触发加载更多（分页）任务 （传空null可以触发刷新任务）
		 * @param adapter 适配器，用于统计当前条数计算分页（传空null可以触发刷新任务）
		 */
		public AbListViewTask(AfListAdapter<T> adapter) {
			super(adapter);
		}

		@Override
		protected List<T> onLoad() {
			List<T> list = AoListViewActivity.this.onTaskLoad();
			if (!AfCollections.isEmpty(list)) {
				return list;
			}
			return super.onLoad();
		}

		//事件转发 参考 AfListViewFragment.onListByPage
		@Override
		protected List<T> onListByPage(Page page, int task) throws Exception {
			return AoListViewActivity.this.onTaskListByPage(page,task);
		}

		//事件转发 参考 AfListViewFragment.onLoaded
		@Override
		protected boolean onLoaded(boolean isfinish, List<T> ltdata,Date cachetime) {
			return AoListViewActivity.this.onLoaded(this,isfinish, ltdata,cachetime);
		}

		//事件转发 参考 AfListViewFragment.onRefreshed
		@Override
		protected boolean onRefreshed(boolean isfinish, List<T> ltdata) {
			return AoListViewActivity.this.onRefreshed(this,isfinish, ltdata);
		}
		//事件转发 参考 AfListViewFragment.onMored
		@Override
		protected boolean onMored(boolean isfinish, List<T> ltdata,
				boolean ended) {
			return AoListViewActivity.this.onMored(this,isfinish, ltdata);
		}

	}
	

	/**
	 * 缓存加载结束处理时间（框架默认调用onRefreshed事件处理）
	 * @author 树朾
	 * @param task 
	 * @param isfinish
	 * @param ltdata
	 * @param cachetime 缓存时间
	 * @return
	 */
	protected boolean onLoaded(AbListViewTask task, boolean isfinish,
			List<T> ltdata, Date cachetime) {
		boolean deal = onRefreshed(task,isfinish,ltdata);
		if (isfinish && !AfCollections.isEmpty(ltdata)) {
			//设置上次刷新缓存时间
			mListView.setLastUpdateTime(cachetime);
		}
		return deal;
	}

	/**
	 * @param task 
	 * 任务刷新结束处理事件
	 * @author 树朾
	 * @param isfinish 是否成功执行
	 * @param ltdata
	 * @return 返回true 已经做好错误页面显示 返回false 框架会做好默认错误反馈
	 */
	@SuppressWarnings("static-access")
	protected boolean onRefreshed(AbListViewTask task, boolean isfinish, List<T> ltdata) {
		if (isfinish) {
			//通知列表刷新完成
			mListView.finishRefresh();
			if (!AfCollections.isEmpty(ltdata)) {
				mAdapter = newAdapter(getActivity(), ltdata);
				setData(mAdapter);
				if (ltdata.size() < task.PAGE_SIZE && mPagingEnable) {
					mListView.removeMoreView();
				}else {
					mListView.addMoreView();
				}
			} else {
				setNodata();
			}
		} else {
			//通知列表刷新失败
			mListView.finishRefreshFail();
			if (mAdapter != null && mAdapter.getCount() > 0) {
				mListView.setAdapter(mAdapter);
				mSelector.selectFrame(mListView);
				makeToastLong(task.makeErrorToast("加载失败"));
			} else {
				setLoadError(task.mException);
			}
		}
		return true;
	}

	/**
	 * 任务加载更多结束处理事件
	 * @author 树朾
	 * @param loadListTask
	 * @param isfinish
	 * @param ltdata
	 * @return 返回true 已经做好错误页面显示 返回false 框架会做好默认错误反馈
	 */
	@SuppressWarnings("static-access")
	protected boolean onMored(AbListViewTask task, boolean isfinish,
			List<T> ltdata) {
		// 通知列表刷新完成
		mListView.finishLoadMore();
		if (isfinish) {
			if (!AfCollections.isEmpty(ltdata)) {
				final int count = mAdapter.getCount();
				// 更新列表
				mAdapter.addData(ltdata);
				mListView.smoothScrollToPosition(count+1);
			}
			if (ltdata.size() < task.PAGE_SIZE) {
				// 关闭更多选项
				makeToastShort("数据全部加载完毕！");
				mListView.removeMoreView();
			}
		} else {
			makeToastLong(task.makeErrorToast("获取更多失败！"));
		}
		return true;
	}

	/**
	 * 获取列表项布局Item
	 * 如果重写 newAdapter 之后，本方法将无效
	 * @param data 对应的数据
	 * @return 实现 布局接口 IAfLayoutItem 的Item兑现
	 * 	new LayoutItem implements IAfLayoutItem<T>(){}
	 */
	protected abstract IAfLayoutItem<T> getItemLayout(T data);

	/**
	 * 加载缓存列表（不分页，在异步线程中执行，不可以更改页面操作）
	 * @author 树朾
	 * @return 返回 null 可以使用框架内置缓存
	 */
	protected List<T> onTaskLoad() {
		return null;
	}

	/**
	 * 数据分页加载（在异步线程中执行，不可以更改页面操作）
	 * @param page 分页对象
	 * @param task 任务id
	 * @return 加载到的数据列表
	 * @throws Exception
	 */
	protected abstract List<T> onTaskListByPage(Page page, int task) throws Exception;

	/**
	 * 根据数据ltdata新建一个 适配器 重写这个方法之后getItemLayout方法将失效
	 * @author 树朾
	 * @param context
	 * @param ltdata
	 * @return
	 */
	protected AfListAdapter<T> newAdapter(Context context, List<T> ltdata) {
		return new AbListViewAdapter(getContext(), ltdata);
	}

	/**
	 * ListView数据适配器（事件已经转发getItemLayout，无实际处理代码）
	 * @author 树朾
	 */
	protected class AbListViewAdapter extends AfListAdapter<T>{

		public AbListViewAdapter(Context context, List<T> ltdata) {
			super(context, ltdata);
		}
		/**
		 * 转发事件到 AoListViewActivity.this.getItemLayout(data);
		 * @author 树朾
		 * @param data
		 * @return 
		 */
		@Override
		protected IAfLayoutItem<T> getItemLayout(T data) {
			return AoListViewActivity.this.getItemLayout(data);
		}
		
	}
}
