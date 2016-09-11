package com.andframe.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private View view;
    private FrameLayout frame;
    private ViewGroup parent;

    public RecyclerViewHolder(ViewGroup parent) {
        super(new FrameLayout(parent.getContext()));
        this.parent = parent;
        this.frame = (FrameLayout) itemView;
    }

    public View getView() {
        return view;
    }

    public ViewGroup getParent() {
        return parent;
    }

    public void setView(View view) {
        if (this.view != view) {
            this.view = view;
            this.frame.addView(view);
        }
    }
}