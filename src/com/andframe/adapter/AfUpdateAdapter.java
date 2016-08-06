package com.andframe.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 可直接更新状态(不刷新列表)的适配器
 * Created by SCWANG on 2016/8/5.
 */
public class AfUpdateAdapter<T> extends AfListAdapterWrapper<T> {

    protected Map<IListItem<T>, Integer> itemTMap = new HashMap<>();

    public AfUpdateAdapter(AfListAdapter<T> wrapped) {
        super(wrapped);
    }

    protected boolean bindingItem(IListItem<T> item, int index) {
        itemTMap.put(item, index);
        return super.bindingItem(item, index);
    }

    public void update() {
        update(null);
    }

    public void update(T model) {
        for (Entry<IListItem<T>, Integer> entry : itemTMap.entrySet()) {
            int index = entry.getValue();
            if (index > -1 && index < getCount()) {
                T itemModel = getItemAt(index);
                if (model == null || itemModel == model) {
                    entry.getKey().onBinding(itemModel, index);
                }
            }
        }
    }

}
