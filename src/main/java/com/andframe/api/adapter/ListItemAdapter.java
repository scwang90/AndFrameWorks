package com.andframe.api.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.andframe.adapter.recycler.ViewHolderItem;

import java.util.List;

/**
 * 可以使用 ListItem 填充 列表控件的 适配器接口
 * Created by SCWANG on 2016/9/10.
 */
public interface ListItemAdapter<T> extends List<T>, ListAdapter, RecyclerAdapter<ViewHolderItem<T>> {

    List<T> getList();
    ListItem<T> newListItem(int viewType);
    View onInflateItem(ListItem<T> item, ViewGroup parent);
    void bindingItem(View view, ListItem<T> item, int index);
    void set(List<T> list);
    void notifyDataSetChanged();
    void notifyDataSetInvalidated();

}
