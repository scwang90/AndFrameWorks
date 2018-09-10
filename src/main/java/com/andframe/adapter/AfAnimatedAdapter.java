package com.andframe.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.andframe.adapter.recycler.ViewHolderItem;
import com.andframe.api.adapter.AnimatedAdapter;
import com.andframe.api.adapter.ItemsViewerAdapter;
import com.andframe.impl.wrapper.ItemsViewerAdapterWrapper;

/**
 * 带动画的适配器
 * Created by SCWANG on 2018/2/1.
 */

@SuppressWarnings("WeakerAccess")
public class AfAnimatedAdapter<T> extends ItemsViewerAdapterWrapper<T> implements AnimatedAdapter<T> {

    private int mLastPosition = -1;
    private boolean mOpenAnimationEnable = true;

    public AfAnimatedAdapter(ItemsViewerAdapter<T> wrapped) {
        super(wrapped);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolderItem<T> holder) {
        super.onViewAttachedToWindow(holder);
        addAnimate(holder.itemView, holder.getLayoutPosition());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = super.getView(i, view, viewGroup);
        addAnimate(view, i);
        return view;
    }

    protected void addAnimate(View view, int position) {
        if (mOpenAnimationEnable && mLastPosition < position) {
            view.setAlpha(0);
            view.animate().alpha(1).start();
            mLastPosition = position;
        }
    }

    @Override
    public void setEnableAnimated(boolean enabled) {
        mOpenAnimationEnable = enabled;
    }
}
