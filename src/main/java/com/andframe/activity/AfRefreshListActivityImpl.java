package com.andframe.activity;

import android.widget.ListView;

import com.andframe.R;
import com.andframe.api.page.Pager;
import com.andframe.module.AfFrameSelector;
import com.andframe.module.AfModuleNodata;
import com.andframe.module.AfModuleNodataImpl;
import com.andframe.module.AfModuleProgress;
import com.andframe.module.AfModuleProgressImpl;
import com.andframe.module.AfModuleTitlebar;
import com.andframe.module.AfModuleTitlebarImpl;

/**
 * 数据列表框架 Activity
 * 带有下拉刷新、数据分页加载、上啦更多、数据缓存
 *
 * @param <T> 列表数据实体类
 * @author 树朾
 */
public abstract class AfRefreshListActivityImpl<T> extends AfRefreshListActivity<T> {

    protected AfModuleTitlebar mTitlebar;

    @SuppressWarnings("unused")
    public AfRefreshListActivityImpl() {
    }

    /**
     * 使用缓存必须调用这个构造函数
     * @param clazz 缓存使用的 class 对象（json要用到）
     */
    @SuppressWarnings("unused")
    public AfRefreshListActivityImpl(Class<T> clazz) {
        super(clazz);
    }

    /**
     * 使用缓存必须调用这个构造函数
     * 	可以自定义缓存标识
     * @param clazz 缓存使用的 class 对象（json要用到）
     */
    @SuppressWarnings("unused")
    public AfRefreshListActivityImpl(Class<T> clazz, String KEY_CACHELIST) {
        super(clazz,KEY_CACHELIST);
    }

    @Override
    protected void onAfterViews() throws Exception {
        super.onAfterViews();
        mTitlebar = newModuleTitlebar(this);
    }

    protected AfModuleTitlebar newModuleTitlebar(Pager pageable) {
        return new AfModuleTitlebarImpl(pageable);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.af_activity_listview;
    }

    @Override
    protected ListView findListView(Pager pageable) {
        return pageable.findViewByID(R.id.listcontent_list);
    }

    @Override
    protected AfFrameSelector newAfFrameSelector(Pager pageable) {
        return new AfFrameSelector(this, R.id.listcontent_contentframe);
    }

    @Override
    protected AfModuleProgress newModuleProgress(Pager pageable) {
        return new AfModuleProgressImpl(pageable);
    }

    @Override
    protected AfModuleNodata newModuleNodata(Pager pageable) {
        return new AfModuleNodataImpl(pageable);
    }

//    @Override
//    protected ListItem<T> newListItem(T data) {
//        return null;
//    }
//
//    @Override
//	protected List<T> onTaskListByPage(Page page, int task) throws Exception {
//		return null;
//	}
}
