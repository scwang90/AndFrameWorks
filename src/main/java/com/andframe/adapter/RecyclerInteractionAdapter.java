package com.andframe.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.andframe.R;
import com.andframe.impl.wrapper.RecyclerAdapterWrapper;
import com.andframe.listener.SafeListener;

import java.lang.ref.WeakReference;

@SuppressWarnings("WeakerAccess")
public class RecyclerInteractionAdapter extends RecyclerAdapterWrapper<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    protected WeakReference<RecyclerView> mRecyclerView;
    protected AdapterView.OnItemClickListener mOnItemClickListener;
    protected AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    public RecyclerInteractionAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> wrapped) {
        super(wrapped);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            mRecyclerView = new WeakReference<>((RecyclerView) parent);
        }
        RecyclerView.ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        if (viewHolder.itemView != null) {
            if (viewHolder.itemView.getBackground() == null) {
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = viewHolder.itemView.getContext().getTheme();
                int top = viewHolder.itemView.getPaddingTop();
                int bottom = viewHolder.itemView.getPaddingBottom();
                int left = viewHolder.itemView.getPaddingLeft();
                int right = viewHolder.itemView.getPaddingRight();
                if (theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
                    viewHolder.itemView.setBackgroundResource(typedValue.resourceId);
                } else {
                    viewHolder.itemView.setBackgroundResource(R.drawable.af_selector_background);
                }
                viewHolder.itemView.setPadding(left, top, right, bottom);
            }
            if (!viewHolder.itemView.isClickable()) {
                viewHolder.itemView.setOnClickListener(new SafeListener((View.OnClickListener)RecyclerInteractionAdapter.this));
                viewHolder.itemView.setOnLongClickListener(new SafeListener((View.OnLongClickListener) RecyclerInteractionAdapter.this));
            }
        }
        return viewHolder;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            RecyclerView recyclerView = mRecyclerView == null ? null : mRecyclerView.get();
            if (recyclerView == null) {
                mOnItemClickListener.onItemClick(null, v, -1, -1);
            } else {
                int position = recyclerView.getChildAdapterPosition(v);
                mOnItemClickListener.onItemClick(null, v, recyclerView.getChildAdapterPosition(v), getItemId(position));
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            RecyclerView recyclerView = mRecyclerView == null ? null : mRecyclerView.get();
            if (recyclerView == null) {
                return mOnItemLongClickListener.onItemLongClick(null, v, -1, -1);
            } else {
                int position = recyclerView.getChildAdapterPosition(v);
                return mOnItemLongClickListener.onItemLongClick(null, v, recyclerView.getChildAdapterPosition(v), getItemId(position));
            }
        }
        return false;
    }
}
