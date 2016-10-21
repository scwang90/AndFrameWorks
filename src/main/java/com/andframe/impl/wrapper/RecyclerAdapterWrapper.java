package com.andframe.impl.wrapper;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerAdapterWrapper
 * Created by SCWANG on 2016/10/21.
 */

public class RecyclerAdapterWrapper<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    protected RecyclerView.Adapter<T> wrapped;

    public RecyclerAdapterWrapper(RecyclerView.Adapter<T> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return wrapped.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        wrapped.onBindViewHolder(holder, position);
    }

    @Override
    public void onBindViewHolder(T holder, int position, List<Object> payloads) {
        wrapped.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return wrapped.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        wrapped.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return wrapped.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return wrapped.getItemCount();
    }

    @Override
    public void onViewRecycled(T holder) {
        wrapped.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(T holder) {
        return wrapped.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(T holder) {
        wrapped.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(T holder) {
        wrapped.onViewDetachedFromWindow(holder);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        wrapped.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        wrapped.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        wrapped.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        wrapped.onDetachedFromRecyclerView(recyclerView);
    }
}
