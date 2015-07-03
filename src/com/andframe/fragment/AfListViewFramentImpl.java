package com.andframe.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.andframe.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.layoutbind.AfFrameSelector;
import com.andframe.layoutbind.AfModuleNodata;
import com.andframe.layoutbind.AfModuleNodataImpl;
import com.andframe.layoutbind.AfModuleProgress;
import com.andframe.layoutbind.AfModuleProgressImpl;


public abstract class AfListViewFramentImpl<T> extends AfListViewFrament<T> {

	public AfListViewFramentImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 使用缓存必须调用这个构造函数
	 * @param clazz
	 */
	public AfListViewFramentImpl(Class<T> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 使用缓存必须调用这个构造函数
	 * 	可以自定义缓存标识
	 * @param clazz
	 */
	public AfListViewFramentImpl(Class<T> clazz, String KEY_CACHELIST) {
		super(clazz,KEY_CACHELIST);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		ViewGroup root = null;
		return inflater.inflate(R.layout.af_module_listview, root);
	}

	@Override
	protected ListView findListView(AfPageable pageable) {
		// TODO Auto-generated method stub
		return pageable.findViewByID(R.id.modulelistview_listview);
	}

	@Override
	protected AfFrameSelector newAfFrameSelector(AfPageable pageable) {
		// TODO Auto-generated method stub
		return new AfFrameSelector(this, R.id.modulelistview_contentframe);
	}

	@Override
	protected AfModuleProgress newModuleProgress(AfPageable pageable) {
		// TODO Auto-generated method stub
		return new AfModuleProgressImpl(pageable);
	}

	@Override
	protected AfModuleNodata newModuleNodata(AfPageable pageable) {
		// TODO Auto-generated method stub
		return new AfModuleNodataImpl(pageable);
	}
}
