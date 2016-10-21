package com.andframe.api.page;

import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;

import com.andframe.api.ListItemAdapter;
import com.andframe.api.multistatus.MoreFooter;
import com.andframe.api.multistatus.OnMoreListener;
import com.andframe.api.view.ItemsViewer;
import com.andframe.model.Page;
import com.andframe.task.AfHandlerTask;

import java.util.List;

/**
 * 基本列表页帮助类接口
 * Created by SCWANG on 2016/9/7.
 */
public interface ItemsPagerHelper<T> extends OnItemClickListener, OnItemLongClickListener, OnMoreListener {
    int getLayoutId();
    void bindAdapter(ItemsViewer listView, ListAdapter adapter);
    void onTaskLoaded(AfHandlerTask task, List<T> list);
    void onTaskMoreLoaded(AfHandlerTask task, List<T> list);
    ListItemAdapter<T> newAdapter(Context context, List<T> list);
    ItemsViewer onViewCreated();
    List<T> onTaskLoadList(Page page);

    MoreFooter<T> newMoreFooter();

    boolean setMoreShow(AfHandlerTask task, List<T> list);
}
