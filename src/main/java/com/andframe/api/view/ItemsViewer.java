package com.andframe.api.view;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

/**
 * 对 listview gridview recyclerview 的抽象接口
 * Created by SCWANG on 2016/9/14.
 */
public interface ItemsViewer<T extends ViewGroup> {
    void smoothScrollToPosition(int index);
    void setOnItemClickListener(AdapterView.OnItemClickListener listener);
    void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener);
    void setAdapter(ListAdapter adapter);

    T getItemsView();
}
