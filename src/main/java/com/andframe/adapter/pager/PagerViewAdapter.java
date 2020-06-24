package com.andframe.adapter.pager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.query.handler.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class PagerViewAdapter<T> extends PagerAdapter {

    //<editor-fold desc="PagerAdapter">

    protected List<T> list;
    protected List<ViewerHolder<T>> holders = Arrays.asList(null,null,null);

    public PagerViewAdapter(Collection<T> collection) {
        this.list = new ArrayList<>(collection);
    }

    public void refresh(Collection<T> collection) {
        list = new ArrayList<>(collection);
        notifyDataSetChanged();
        for (ViewerHolder<T> holder : holders) {
            if (holder != null) {
                holder.refresh();
            }
        }
    }

    public void set(int index, T t) {
        list.set(index, t);
        notifyDataSetChanged();
        ViewerHolder<T> holder = holders.get(index % holders.size());
        if (holder != null && holder.index == index) {
            holder.refresh();
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        if (object instanceof ViewerHolder) {
            return ((ViewerHolder) object).view == view;
        }
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int index  = position % holders.size();
        ViewerHolder<T> holder = holders.get(index);
        if (holder == null) {
            holder = new ViewerHolder<>();
            holder.viewer = newItemViewer();
            holder.view = holder.viewer.onCreateView(container.getContext(), container);
            holders.set(index, holder);
            container.addView(holder.view);
        }
        holder.bind(list.get(position), position);
        return holder;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }

    public abstract ItemViewer<T> newItemViewer();

    public void remove(Filter<T> filter) {
        for (int i = 0; i < list.size(); i++) {
            if (filter.filter(list.get(i))) {
                list.remove(i--);
            }
        }
        notifyDataSetChanged();
    }

    //</editor-fold>

    private static class ViewerHolder<T> {
        private T model;
        private int index;
        private View view;
        private ItemViewer<T> viewer;

        public void bind(T model, int index) {
            this.model = model;
            this.index = index;
            viewer.onBinding(view, model, index);
        }

        public void refresh() {
            viewer.onBinding(view, model, index);
        }
    }
}