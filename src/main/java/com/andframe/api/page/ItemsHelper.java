package com.andframe.api.page;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;

import com.andframe.adapter.AfHeaderFooterAdapter;
import com.andframe.api.adapter.ListItemAdapter;
import com.andframe.api.multistatus.MoreFooter;
import com.andframe.api.multistatus.OnMoreListener;
import com.andframe.api.multistatus.OnRefreshListener;
import com.andframe.api.view.ItemsViewer;
import com.andframe.api.view.ViewQuery;
import com.andframe.task.AfHandlerTask;

import java.util.Date;
import java.util.List;

/**
 * 基本列表页帮助类接口
 * Created by SCWANG on 2016/9/7.
 */
public interface ItemsHelper<T> extends MultiStatusHelper<List<T>>, OnItemClickListener, OnItemLongClickListener, OnRefreshListener, OnMoreListener {

    ViewQuery<? extends ViewQuery> $(int id, int... ids);

    void bindAdapter(ItemsViewer listView, ListAdapter adapter);
    void onTaskLoadedCache(AfHandlerTask task, List<T> list);
    void onTaskLoadedRefresh(AfHandlerTask task, List<T> list);
    void onTaskLoadedMore(AfHandlerTask task, List<T> list);
    void onViewCreated() throws Exception;

    ListItemAdapter<T> initAdapter();
    ListItemAdapter<T> newAdapter(Context context, List<T> list);
//    List<T> onTaskLoadList(Page page);
    List<T> onTaskLoadCache(boolean isCheckExpired);

    MoreFooter newMoreFooter();
    ItemsViewer findItemsViewer(ItemsPager<T> pager, View contentView);

    boolean setMoreShow(AfHandlerTask task, List<T> list);

    void initCache();
    void clearCache();
    void putCache();
    void putCache(List<T> list);
    Date getCacheTime();

    void onTaskPutCache(List<T> list);
    void onTaskPushCache(List<T> list);

    void finishRefresh();

    void finishRefreshFail();

    void setLastRefreshTime(Date time);

    void bindListHeaderAndFooter(AfHeaderFooterAdapter<T> adapter);

}
