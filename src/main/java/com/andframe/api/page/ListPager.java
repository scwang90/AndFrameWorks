package com.andframe.api.page;

import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;

import com.andframe.api.ListItem;
import com.andframe.api.ListItemAdapter;
import com.andframe.api.view.ItemsViewer;
import com.andframe.task.AfHandlerTask;

import java.util.List;

/**
 * 基本列表页
 * Created by SCWANG on 2016/9/7.
 */
public interface ListPager<T> extends Pager, OnItemClickListener, OnItemLongClickListener {

    void onTaskLoaded(AfHandlerTask task, List<T> list);
    List<T> onTaskLoadList() throws Exception;

    ListItem<T> newListItem();
    ItemsViewer findItemsViewer(ListPager<T> pager);
    ListItemAdapter<T> newAdapter(Context context, List<T> list);
    void bindListHeaderAndFooter();
    void bindAdapter(ItemsViewer listView, ListAdapter adapter);

    boolean onItemLongClick(T model, int index);
    void onItemClick(T model, int index);

}
