package com.andframe.api;

import android.widget.ListAdapter;

import java.util.List;

/**
 * 可以使用 ListItem 填充 列表控件的 适配器接口
 * Created by SCWANG on 2016/9/10.
 */
public interface ListItemAdapter<T> extends List<T>, ListAdapter {

    List<T> getList();
    void set(List<T> list);
    void notifyDataSetChanged();
}
