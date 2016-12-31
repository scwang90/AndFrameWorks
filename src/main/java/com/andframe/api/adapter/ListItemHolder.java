package com.andframe.api.adapter;

/**
 * ItemViewer 和 ViewHolder 的结合
 * Created by SCWANG on 2016/10/29.
 */

public interface ListItemHolder<T> {
    ItemViewer<T> getItem();
}
