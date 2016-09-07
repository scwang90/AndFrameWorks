package com.andframe.api.page;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;

import com.andframe.adapter.AfListAdapter;
import com.andframe.api.ListItem;
import com.andframe.task.AfHandlerTask;

import java.util.List;

/**
 * 基本列表页
 * Created by SCWANG on 2016/9/7.
 */
public interface ListPager<T> extends Pager, OnItemClickListener, OnItemLongClickListener {

    void onTaskLoaded(AfHandlerTask task, List<T> list);
    List<T> onTaskLoadList() throws Exception;

    ListItem<T> newListItem(T model);
    AbsListView findListView(ListPager<T> pager);
    AfListAdapter<T> newAdapter(Context context, List<T> list);
    void bindAdapter(AbsListView listView, ListAdapter adapter);

    boolean onItemLongClick(T model, int index);
    void onItemClick(T model, int index);
}
