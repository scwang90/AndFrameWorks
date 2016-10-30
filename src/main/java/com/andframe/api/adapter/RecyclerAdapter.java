package com.andframe.api.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerAdapter
 * Created by SCWANG on 2016/10/29.
 */

interface RecyclerAdapter<VH/* extends RecyclerView.ViewHolder*/> {

    VH createViewHolder(ViewGroup parent, int viewType);
    VH onCreateViewHolder(ViewGroup parent, int viewType);
    void onBindViewHolder(VH holder, int position);
    void onBindViewHolder(VH holder, int position, List<Object> payloads);
    void bindViewHolder(VH holder, int position);
    int getItemViewType(int position);
    void setHasStableIds(boolean hasStableIds);
    long getItemId(int position);
    int getItemCount();
    boolean hasStableIds();
    void onViewRecycled(VH holder);
    boolean onFailedToRecycleView(VH holder);
    void onViewAttachedToWindow(VH holder);
    void onViewDetachedFromWindow(VH holder);
    boolean hasObservers();
    void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer);
    void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer);
    void onAttachedToRecyclerView(RecyclerView recyclerView);
    void onDetachedFromRecyclerView(RecyclerView recyclerView);
    void notifyDataSetChanged();
    void notifyItemChanged(int position);
    void notifyItemChanged(int position, Object payload);
    void notifyItemRangeChanged(int positionStart, int itemCount);
    void notifyItemRangeChanged(int positionStart, int itemCount, Object payload);
    void notifyItemInserted(int position);
    void notifyItemMoved(int fromPosition, int toPosition);
    void notifyItemRangeInserted(int positionStart, int itemCount);
    void notifyItemRemoved(int position);
    void notifyItemRangeRemoved(int positionStart, int itemCount);
}
