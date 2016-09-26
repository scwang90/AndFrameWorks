package com.andframe.api.page;

import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;

import com.andframe.api.ListItemAdapter;
import com.andframe.api.view.ItemsViewer;
import com.andframe.task.AfHandlerTask;

import java.util.List;

/**
 * 基本列表页帮助类接口
 * Created by SCWANG on 2016/9/7.
 */
public interface ListPagerHelper<T> extends OnItemClickListener, OnItemLongClickListener {
    int getLayoutId();
    void onAfterViews();
    void bindAdapter(ItemsViewer listView, ListAdapter adapter);
    ListItemAdapter<T> newAdapter(Context context, List<T> list);
    void onTaskLoaded(AfHandlerTask task, List<T> list);
    List<T> onTaskLoadList();
}
