package com.andframe.activity;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;

import com.andframe.activity.framework.AfPageable;
import com.andframe.adapter.AfListAdapter;
import com.andframe.adapter.AfListAdapter.IAfLayoutItem;
import com.andframe.layoutbind.AfSelectorBottombar;
import com.andframe.layoutbind.AfSelectorTitlebar;
import com.andframe.view.AfMultiGridView;
import com.andframe.view.AfMultiListView;
import com.andframe.view.AfRefreshAbsListView;
import com.andframe.view.multichoice.AfMultiChoiceAdapter;
import com.andframe.view.multichoice.AfMultiChoiceItem;
import com.andframe.view.multichoice.AfMultiChoiceAbsListView;
import com.andframe.widget.popupmenu.OnMenuItemClickListener;

import java.util.List;

/**
 * 数据列表多选框架 Activity
 * 提供选择状态，多选删除
 * @author 树朾
 * @param <T> 列表数据实体类
 */
public abstract class AfMultiChoiceListActivity<T> extends AfListViewActivity<T> implements OnMenuItemClickListener {

	protected AfSelectorTitlebar mSelectorTitlebar;
	protected AfSelectorBottombar mSelectorBottombar;

	protected AfMultiChoiceAdapter<T> mMultiChoiceAdapter;
	protected AfMultiChoiceAbsListView<? extends AbsListView> mMultiChoiceListView;

	public AfMultiChoiceListActivity() {

	}

	/**
	 * 使用缓存必须调用这个构造函数
	 * @param clazz 缓存使用的 class 对象（json要用到）
	 */
	public AfMultiChoiceListActivity(Class<T> clazz) {
		super(clazz);
	}

	/**
	 * 使用缓存必须调用这个构造函数
	 * 	可以自定义缓存标识
	 * @param clazz 缓存使用的 class 对象（json要用到）
	 */
	public AfMultiChoiceListActivity(Class<T> clazz, String KEY_CACHELIST) {
		super(clazz,KEY_CACHELIST);
	}

	/**
	 * 创建新的 标题选择器 SelectorTitlebar
	 */
	protected abstract AfSelectorTitlebar newSelectorTitlebar(AfPageable pageable);
	/**
	 * 创建新的 底部选择器 SelectorBottombar
	 */
	protected abstract AfSelectorBottombar newSelectorBottombar(AfPageable pageable);
	/**
	 * 获取多选项
	 */
	protected abstract AfMultiChoiceItem<T> getMultiChoiceItemLayout(T data);

	/**
	 * 拦截选择模式的返回事件 映射到结束选择事件
	 */
	@Override
	protected boolean onBackKeyPressed() {
		if (mMultiChoiceAdapter != null && mMultiChoiceAdapter.isMultiChoiceMode()) {
			mMultiChoiceAdapter.closeMultiChoice();
			return true;
		}
		return super.onBackKeyPressed();
	}

	@Override
	protected IAfLayoutItem<T> getItemLayout(T data) {
		return getMultiChoiceItemLayout(data);
	}

	@Override
	protected AfListAdapter<T> newAdapter(Context context, List<T> ltdata) {
		return mMultiChoiceAdapter = new AbMultiChoiceAdapter(getContext(), ltdata);
	}

	@Override
	protected AfRefreshAbsListView<? extends AbsListView> newAfListView(AfPageable pageable) {
		AbsListView listView = findListView(pageable);
		if (listView instanceof ListView) {
			mMultiChoiceListView = new AfMultiListView(((ListView) listView));
		} else if (listView instanceof GridView) {
			mMultiChoiceListView = new AfMultiGridView(((GridView) listView));
		} else {
			mMultiChoiceListView = new AfMultiListView(getContext());
		}
		mSelectorTitlebar = newSelectorTitlebar(pageable);
		mSelectorBottombar = newSelectorBottombar(pageable);
		if (mSelectorTitlebar != null){
			mSelectorTitlebar.setMenuItemListener(this);
			mMultiChoiceListView.setSelector(mSelectorTitlebar);
		}
		if (mSelectorBottombar != null){
			mSelectorBottombar.setMenuItemListener(this);
			mMultiChoiceListView.setSelector(mSelectorBottombar);
		}
		return mMultiChoiceListView;
	}

	/**
	 *  ListView数据适配器（事件已经转发getItemLayout，无实际处理代码）
	 */
	protected class AbMultiChoiceAdapter extends AfMultiChoiceAdapter<T>{

		public AbMultiChoiceAdapter(Context context, List<T> ltdata) {
			super(context, ltdata);
		}
		/**
		 *  转发事件到 AfListViewActivity.this.getItemLayout(data);
		 */
		@Override
		protected AfMultiChoiceItem<T> getMultiChoiceItem(T data) {
			return AfMultiChoiceListActivity.this.getMultiChoiceItemLayout(data);
		}

	}
}
