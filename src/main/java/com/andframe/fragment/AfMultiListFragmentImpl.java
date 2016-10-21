package com.andframe.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.andframe.R;
import com.andframe.api.page.ListPager;
import com.andframe.api.page.MultiListPager;
import com.andframe.api.view.ItemsViewer;
import com.andframe.impl.viewer.ItemsGridViewWrapper;
import com.andframe.impl.viewer.ItemsListViewWrapper;
import com.andframe.impl.viewer.ItemsRecyclerViewWrapper;
import com.andframe.module.AfFrameSelector;
import com.andframe.module.AfModuleNodata;
import com.andframe.module.AfModuleNodataImpl;
import com.andframe.module.AfModuleProgress;
import com.andframe.module.AfModuleProgressImpl;
import com.andframe.module.AfModuleTitlebar;
import com.andframe.module.AfModuleTitlebarImpl;

/**
 * 多功能列表页 实现默认页面
 * 1.多页面数据（数据页、空数据页、加载页）
 * 2.下拉刷新（下拉，上拉）
 * 3.分页加载数据 (TaskByPage)
 * 4.基本数据缓存（Cache）
 * Created by SCWANG on 2016/9/7.
 */
public abstract class AfMultiListFragmentImpl<T> extends AfMultiListFragment<T> {

    protected AfModuleTitlebar mTitlebar;

    @SuppressWarnings("unused")
    public AfMultiListFragmentImpl() {
    }

    @Override
    public void onViewCreated() throws Exception {
        super.onViewCreated();
        mTitlebar = newModuleTitlebar(this);
    }

    public AfModuleTitlebar newModuleTitlebar(MultiListPager<T> pager) {
        return new AfModuleTitlebarImpl(pager);
    }

    @Override
    public int getLayoutId() {
        int layoutId = super.getLayoutId();
        if (layoutId <= 0) {
            layoutId = R.layout.af_activity_listview;
        }
        return layoutId;
    }

    @Override
    public ItemsViewer findItemsViewer(ListPager<T> pager) {
        View view = pager.findViewByID(R.id.listcontent_list);
        if (view instanceof ListView) {
            return new ItemsListViewWrapper(((ListView) view));
        } else if (view instanceof GridView) {
            return new ItemsGridViewWrapper(((GridView) view));
        } else if (view instanceof RecyclerView) {
            return new ItemsRecyclerViewWrapper(((RecyclerView) view));
        }
        return null;
    }

    @Override
    public AfFrameSelector newAfFrameSelector(MultiListPager<T> pager) {
        return new AfFrameSelector(this, R.id.listcontent_contentframe);
    }

    @Override
    public AfModuleProgress newModuleProgress(MultiListPager<T> pager) {
        return new AfModuleProgressImpl(pager);
    }

    @Override
    public AfModuleNodata newModuleNodata(MultiListPager<T> pager) {
        return new AfModuleNodataImpl(pager);
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
