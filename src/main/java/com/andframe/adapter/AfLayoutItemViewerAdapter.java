package com.andframe.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.annotation.interpreter.LayoutBinder;

import java.util.List;

/**
 * 自带布局的适配器
 * Created by SCWANG on 2016/9/2.
 */
@SuppressWarnings("unused")
public abstract class AfLayoutItemViewerAdapter<T> extends AfItemsViewerAdapter<T> {

    int mLayoutId = -1;

    public AfLayoutItemViewerAdapter(List<T> list) {
        super(list);
    }

    public AfLayoutItemViewerAdapter(List<T> list, boolean dataSync) {
        super(list, dataSync);
    }

    public AfLayoutItemViewerAdapter(@LayoutRes int layoutId, List<T> list) {
        super(list);
        mLayoutId = layoutId;
    }

    public AfLayoutItemViewerAdapter(@LayoutRes int layoutId, List<T> list, boolean dataSync) {
        super(list, dataSync);
        mLayoutId = layoutId;
    }

    @Override
    protected View onCreateItemView(Context context, ViewGroup parent) {
        if (mLayoutId == -1) {
            int layoutId = LayoutBinder.getBindLayoutId(this, context);
            if (layoutId > 0) {
                mLayoutId = layoutId;
            } else {
                throw new RuntimeException("请使用BindLayout注解标记你的适配器！");
            }
        }
        return LayoutInflater.from(context).inflate(mLayoutId, parent, false);
    }

}
