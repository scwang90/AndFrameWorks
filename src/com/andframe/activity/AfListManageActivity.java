package com.andframe.activity;

import android.content.DialogInterface;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据列表管理框架 Activity
 * 提供选择管理基本任务分流和处理
 * @author 树朾
 * @param <T> 列表数据实体类
 */
public abstract class AfListManageActivity<T> extends AfMultiChoiceListActivity<T> {
	/**
	 * 模块列表 任务类型枚举
	 */
	protected static final int TASK_DELETE = 10000;
	protected static final int TASK_READ = 20000;
	protected static final int TASK_MODIFY = 30000;
	/**
	 * 模块列表请求 类型 枚举
	 */
	protected static final int REQUEST_DELETE = 10;
	/**
	 * 当前选择项 用于 删除和阅读任务
	 */
	protected T mSelected = null;
	protected List<T> mltDelete = null;

	public AfListManageActivity() {

	}

	/**
	 * 使用缓存必须调用这个构造函数
	 * @param clazz 缓存使用的 class 对象（json要用到）
	 */
	public AfListManageActivity(Class<T> clazz) {
		super(clazz);
	}

	/**
	 * 使用缓存必须调用这个构造函数
	 * 	可以自定义缓存标识
	 * @param clazz 缓存使用的 class 对象（json要用到）
	 */
	public AfListManageActivity(Class<T> clazz, String KEY_CACHELIST) {
		super(clazz,KEY_CACHELIST);
	}

	/**
	 * 创建删除菜单
	 */
	protected void buildMultiDelete() {
		if (mSelectorTitlebar != null){
			mSelectorTitlebar.putMenu("删除", REQUEST_DELETE);
			mSelectorTitlebar.setMenuItemListener(this);
		}
	}

	/**
	 * 向列表中添加数据 到第一条
	 * @param value 数据
	 */
	protected void addData(T value) {
		if (mAdapter == null || mAdapter.getCount() == 0) {
			List<T> mltArray = new ArrayList<T>();
			mltArray.add(value);
			mAdapter = newAdapter(this,mltArray);
			mListView.setAdapter(mAdapter);
			mSelector.SelectFrame(mNodata);
		} else {
			mAdapter.insert(0, value);
		}
	}
	/**
	 * 菜单事件处理
	 * @param item 菜单项
	 * @return 是否处理
	 */
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
			case REQUEST_DELETE:
				this.doReauestDelete();
				break;
		}
		return false;
	}
	/**
	 * 点击删除按钮 提问确认删除
	 */
	protected void doReauestDelete() {
		if (mMultiChoiceAdapter.isMultiChoiceMode()) {
			final List<T> list = mMultiChoiceAdapter.peekSelectedItems();
			if (list.size() > 0) {
				doShowDialog("提问","确定要"+list.size()+"删除个对象吗？", "删除",
						new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0,int arg1) {
								mSelected = null;
								mltDelete = list;
								postTask(TASK_DELETE);
							}
						}, "取消", null);
			} else {
				makeToastLong("还没有选择任何项喔~");
			}
		}
	}

	/**
	 * 列表项被点击
	 * @param model
	 * @param index
	 */
	@Override
	protected void onItemClick(T model, int index) {
		super.onItemClick(model, index);
		mSelected = model;
		postTask(TASK_READ);
	}

	/**
	 * 任务准备开始 （在UI线程中）
	 * @return 返回true 表示准备完毕 否则 false 任务将被取消
	 * @param task 任务ID
	 * @return
	 */
	@Override
	protected boolean onTaskPrepare(int task) {
		switch (task) {
			case TASK_DELETE:
				showProgressDialog("正在删除...");
				if (mltDelete == null && mSelected != null) {
					mltDelete = new ArrayList<T>();
					mltDelete.add(mSelected);
				}
				return true;
		}
		return super.onTaskPrepare(task);
	}

	/**
	 * 任务执行分流（异步线程）
	 * @param task 任务ID
	 * @return 是否执行
	 * @throws Exception
	 */
	@Override
	protected boolean onTaskWorking(int task) throws Exception{
		switch (task) {
			case TASK_READ:
				onTaskRead(mSelected);
				return true;
			case TASK_DELETE:
				if (mltDelete != null && mltDelete.size() > 0) {
					onTaskDelete(mltDelete);
				}
				return true;
		}
		return super.onTaskWorking(task);
	}

	/**
	 * 标记已读任务（异步线程）
	 * @param model
	 * @throws Exception
	 */
	protected void onTaskRead(T model) throws Exception {
		Thread.sleep(1000);
	}

	/**
	 * 删除任务（异步线程）
	 * @param ltdelete
	 * @throws Exception
	 */
	protected void onTaskDelete(List<T> ltdelete) throws Exception {
		Thread.sleep(1000);
	}

	@Override
	protected boolean onTaskWorked(AbListViewTask task, boolean isfinish, List<T> ltdata) {
		switch (task.mTask) {
			case TASK_DELETE:
				hideProgressDialog();
				if (isfinish) {
					makeToastShort("删除成功！");
					List<T> mltArray = mAdapter.getList();
					boolean ret = onDatasDeleted(mltDelete);
					for (T model : mltDelete) {
						if (!ret) {
							onDataDeleted(model);
						}
						mltArray.remove(model);
					}
					mMultiChoiceAdapter.setData(mltArray);
					mMultiChoiceAdapter.closeMultiChoice();
				} else {
					makeToastShort(task.makeErrorToast("删除失败"));
				}
				mltDelete = null;
				return true;
			case TASK_READ:
				if (isfinish) {
					mAdapter.notifyDataSetChanged();
				}
				return true;
		}
		return super.onTaskWorked(task, isfinish, ltdata);
	}

	/**
	 * 数据已经被删除
	 * @param ltDelete
	 * return true onDataDeleted将不会被调用
	 */
	protected boolean onDatasDeleted(List<T> ltDelete) {
		return false;
	}

	/**
	 * 数据已经被删除
	 * @param model
	 */
	protected void onDataDeleted(T model) {

	}

}
