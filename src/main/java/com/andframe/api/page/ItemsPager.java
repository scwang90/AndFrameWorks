package com.andframe.api.page;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;

import com.andframe.adapter.AfHeaderFooterAdapter;
import com.andframe.api.adapter.ListItem;
import com.andframe.api.adapter.ListItemAdapter;
import com.andframe.api.multistatus.MoreFooter;
import com.andframe.api.multistatus.OnMoreListener;
import com.andframe.api.view.ItemsViewer;
import com.andframe.model.Page;
import com.andframe.task.AfHandlerTask;

import java.util.Date;
import java.util.List;

/**
 * 基本列表页
 * Created by SCWANG on 2016/9/7.
 */
public interface ItemsPager<T> extends MultiStatusPager<List<T>>, OnItemClickListener, OnItemLongClickListener, OnMoreListener {

    ListItem<T> newListItem();
    ItemsViewer findItemsViewer(ItemsPager<T> pager, View contentView);
    ListItemAdapter<T> newAdapter(Context context, List<T> list);
    ListItemAdapter<T> initAdapter();

    List<T> onTaskLoadCache(boolean isCheckExpired);
    List<T> onTaskLoadList(Page page) throws Exception;
    void onTaskLoadedCache(AfHandlerTask task, List<T> list);
    void onTaskLoadedMore(AfHandlerTask task, List<T> list);

    void bindListHeaderAndFooter(AfHeaderFooterAdapter<T> adapter);
    void bindAdapter(ItemsViewer listView, ListAdapter adapter);

    boolean onItemLongClick(T model, int index);
    void onItemClick(T model, int index);

    MoreFooter newMoreFooter();

    boolean setMoreShow(AfHandlerTask task, List<T> list);

    void onTaskLoadedRefresh(AfHandlerTask task, List<T> list);
    void onTaskPutCache(List<T> list);
    void onTaskPushCache(List<T> list);

    void finishRefresh();
    void finishRefreshFail();
    void setLastRefreshTime(Date time);

    void initCache();
    void putCache();
    void putCache(List<T> list);
    void clearCache();

    Date getCacheTime();

    String getCacheKey();
}
