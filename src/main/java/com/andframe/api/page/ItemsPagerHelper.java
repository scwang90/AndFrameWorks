package com.andframe.api.page;

import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;

import com.andframe.api.ListItemAdapter;
import com.andframe.api.multistatus.MoreFooter;
import com.andframe.api.multistatus.OnMoreListener;
import com.andframe.api.multistatus.OnRefreshListener;
import com.andframe.api.view.ItemsViewer;
import com.andframe.model.Page;
import com.andframe.task.AfHandlerTask;

import java.util.Date;
import java.util.List;

/**
 * 基本列表页帮助类接口
 * Created by SCWANG on 2016/9/7.
 */
public interface ItemsPagerHelper<T> extends OnItemClickListener, OnItemLongClickListener, OnRefreshListener, OnMoreListener {

    void bindAdapter(ItemsViewer listView, ListAdapter adapter);
    void onTaskLoadedCache(AfHandlerTask task, List<T> list);
    void onTaskLoadedRefresh(AfHandlerTask task, List<T> list);
    void onTaskLoadedMore(AfHandlerTask task, List<T> list);
    ItemsViewer onViewCreated();
    ListItemAdapter<T> newAdapter(Context context, List<T> list);
    List<T> onTaskLoadList(Page page);
    List<T> onTaskLoadCache(boolean isCheckExpired);

    MoreFooter<T> newMoreFooter();
    ItemsViewer findItemsViewer(ItemsPager<T> pager);

    boolean setMoreShow(AfHandlerTask task, List<T> list);

    void initCache();
    void clearCache();
    void putCache();
    void putCache(List<T> list);
    Date getCacheTime();

    void onTaskPutCache(List<T> list);
    void onTaskPushCache(List<T> list);

}
