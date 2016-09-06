package com.andframe.adapter;

import android.view.View;

import com.andframe.api.ListItem;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

/**
 * 可直接更新状态(不刷新列表)的适配器
 * Created by SCWANG on 2016/8/5.
 */
@SuppressWarnings("unused")
public class AfUpdateAdapter<T> extends AfListAdapterWrapper<T> {

    protected Map<View, SimpleEntry<Integer, ListItem<T>>> itemTMap = new HashMap<>();

    public AfUpdateAdapter(AfListAdapter<T> wrapped) {
        super(wrapped);
    }

    protected boolean bindingItem(View view, ListItem<T> item, int index) {
        itemTMap.put(view, new SimpleEntry<>(index, item));
        return super.bindingItem(view, item, index);
    }

    public void update() {
        update(null);
    }

    public void update(int index) {
        update(getItemAt(index));
    }

    public void update(T model) {
        for (Map.Entry<View, SimpleEntry<Integer, ListItem<T>>> entry : itemTMap.entrySet()) {
            int index = entry.getValue().getKey();
            if (index > -1 && index < getCount()) {
                T itemModel = getItemAt(index);
                if (model == null || itemModel == model) {
                    onUpdate(entry.getKey(), entry.getValue().getValue(), index, itemModel);
                }
            }
        }
    }

    protected void onUpdate(View view, ListItem<T> item, int index, T model) {
        item.onBinding(view, model, index);
    }

}
