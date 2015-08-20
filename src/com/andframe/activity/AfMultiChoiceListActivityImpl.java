package com.andframe.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.andframe.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.feature.AfIntent;
import com.andframe.layoutbind.AfFrameSelector;
import com.andframe.layoutbind.AfModuleNodata;
import com.andframe.layoutbind.AfModuleNodataImpl;
import com.andframe.layoutbind.AfModuleProgress;
import com.andframe.layoutbind.AfModuleProgressImpl;
import com.andframe.layoutbind.AfModuleTitlebar;
import com.andframe.layoutbind.AfSelectorBottombar;
import com.andframe.layoutbind.AfSelectorBottombarImpl;
import com.andframe.layoutbind.AfSelectorTitlebar;
import com.andframe.layoutbind.AfSelectorTitlebarImpl;

/**
 * 数据列表管理框架 Activity
 * 提供选择状态，多选删除
 * @author 树朾
 * @param <T> 列表数据实体类
 */
public abstract class AfMultiChoiceListActivityImpl<T> extends AfMultiChoiceListActivity<T> {

	private static final int ID_SELECT = 10;

	protected AfModuleTitlebar mAfTitlebar;

	public AfMultiChoiceListActivityImpl() {
	}

	/**
	 * 使用缓存必须调用这个构造函数
	 * @param clazz
	 */
	public AfMultiChoiceListActivityImpl(Class<T> clazz) {
		super(clazz);
	}

	/**
	 * 使用缓存必须调用这个构造函数
	 * 	可以自定义缓存标识
	 * @param clazz
	 */
	public AfMultiChoiceListActivityImpl(Class<T> clazz, String KEY_CACHELIST) {
		super(clazz,KEY_CACHELIST);
	}

	@Override
	protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
		super.onCreate(bundle, intent);
		mAfTitlebar = new AfModuleTitlebar(this);
		mAfTitlebar.putMenu("选择", ID_SELECT);
		mAfTitlebar.setFunction(AfModuleTitlebar.FUNCTION_MENU);
		mAfTitlebar.setMenuItemListener(this);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.af_activity_listview;
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

	@Override
	protected AfSelectorTitlebar newSelectorTitlebar(AfPageable pageable) {
		return new AfSelectorTitlebarImpl(pageable);
	}

	@Override
	protected AfSelectorBottombar newSelectorBottombar(AfPageable pageable) {
		return new AfSelectorBottombarImpl(pageable);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (mMultiChoiceAdapter != null){
			if (item.getItemId() == ID_SELECT){
				if (!mMultiChoiceAdapter.isMultiChoiceMode()){
					mMultiChoiceAdapter.beginMultiChoice();
					return true;
				}
			}
		}
		return false;
	}

//	@Override
//	protected List<T> onTaskListByPage(Page page, int task) throws Exception {
//		return null;
//	}

//	@Override
//	protected AfMultiChoiceItem<T> getMultiChoiceItemLayout(T data) {
//		return null;
//	}

}
