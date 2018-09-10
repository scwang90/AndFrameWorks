package com.andframe.adapter;

import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.adapter.recycler.ViewHolderItem;
import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.adapter.ItemsViewerAdapter;
import com.andframe.impl.wrapper.ItemsViewerAdapterWrapper;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;

/**
 * 可直接更新状态(不刷新列表)的适配器
 * Created by SCWANG on 2016/8/5.
 */
@SuppressWarnings("unused")
public class AfUpdateItemViewerAdapter<T> extends ItemsViewerAdapterWrapper<T> {

    protected static final int KEY_VIEW_TAG = "RecyclerBaseAdapter".hashCode();

    protected ArrayMap<View, SimpleEntry<Integer, ItemViewer<T>>> itemTMap = new ArrayMap<>();

    public AfUpdateItemViewerAdapter(ItemsViewerAdapter<T> wrapped) {
        super(wrapped);
    }

    //<editor-fold desc="逻辑连接">
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderItem<T> holder = null;
        if (view != null) {
            //noinspection unchecked
            holder = (ViewHolderItem<T>) view.getTag(KEY_VIEW_TAG);
        }
        if (holder == null) {
            holder = createViewHolder(viewGroup, getItemViewType(i));
            holder.itemView.setTag(KEY_VIEW_TAG, holder);
        }
        bindViewHolder(holder, i);
        return holder.itemView;
    }
    @Override
    public void onBindViewHolder(ViewHolderItem<T> holder, int position, List<Object> payloads) {
        onBindViewHolder(holder, position);
    }
    @Override
    public void onBindViewHolder(ViewHolderItem<T> holder, int position) {
        bindingItem(holder.itemView, holder.getItem(), position);
    }
    @Override
    public void bindingItem(View view, ItemViewer<T> item, int index) {
        itemTMap.put(view, new SimpleEntry<>(index, item));
        super.bindingItem(view, item, index);
    }
    //</editor-fold>

    //<editor-fold desc="功能方法">
    public void update() {
        update(null);
    }

    public void update(int index) {
        update(get(index));
    }

    public void update(T model) {
        for (Map.Entry<View, SimpleEntry<Integer, ItemViewer<T>>> entry : itemTMap.entrySet()) {
            int index = entry.getValue().getKey();
            if (index > -1 && index < getCount()) {
                T itemModel = get(index);
                if (model == null || itemModel == model) {
                    onUpdate(entry.getKey(), entry.getValue().getValue(), index, itemModel);
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="子类重写">
    protected void onUpdate(View view, ItemViewer<T> item, int index, T model) {
        item.onBinding(view, model, index);
    }
    //</editor-fold>

}
