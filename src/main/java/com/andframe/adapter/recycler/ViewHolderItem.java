package com.andframe.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.andframe.api.ListItem;

/**
 *
 * Created by SCWANG on 2016/9/10.
 */
public class ViewHolderItem<T> extends RecyclerView.ViewHolder {

    private final ListItem<T> item;

    public ViewHolderItem(ListItem<T> item, Context context, ViewGroup parent) {
        super(item.onCreateView(context, parent));
        this.item = item;
    }

    public ListItem<T> getItem() {
        return item;
    }
}
