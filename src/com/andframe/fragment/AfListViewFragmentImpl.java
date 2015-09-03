package com.andframe.fragment;

import android.widget.ListView;

import com.andframe.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.layoutbind.AfFrameSelector;
import com.andframe.layoutbind.AfModuleNodata;
import com.andframe.layoutbind.AfModuleNodataImpl;
import com.andframe.layoutbind.AfModuleProgress;
import com.andframe.layoutbind.AfModuleProgressImpl;


public abstract class AfListViewFragmentImpl<T> extends AfListViewFragment<T> {

	public AfListViewFragmentImpl() {
	}

	/**
	 * 使用缓存必须调用这个构造函数
	 * @param clazz
	 */
	public AfListViewFragmentImpl(Class<T> clazz) {
		super(clazz);
	}

	/**
	 * 使用缓存必须调用这个构造函数
	 * 	可以自定义缓存标识
	 * @param clazz
	 */
	public AfListViewFragmentImpl(Class<T> clazz, String KEY_CACHELIST) {
		super(clazz,KEY_CACHELIST);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.af_module_listview;
	}

	@Override
	protected ListView findListView(AfPageable pageable) {
		return pageable.findViewByID(R.id.modulelistview_listview);
	}

	@Override
	protected AfFrameSelector newAfFrameSelector(AfPageable pageable) {
		return new AfFrameSelector(this, R.id.modulelistview_contentframe);
	}

	@Override
	protected AfModuleProgress newModuleProgress(AfPageable pageable) {
		return new AfModuleProgressImpl(pageable);
	}

	@Override
	protected AfModuleNodata newModuleNodata(AfPageable pageable) {
		return new AfModuleNodataImpl(pageable);
	}
}
