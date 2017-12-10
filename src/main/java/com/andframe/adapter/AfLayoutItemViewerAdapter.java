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
public abstract class AfLayoutItemViewerAdapter<T> extends AfItemViewerAdapter<T> {

    int mLayoutId = -1;

    public AfLayoutItemViewerAdapter(List<T> ltdata) {
        super(ltdata);
    }

    public AfLayoutItemViewerAdapter(List<T> ltdata, boolean dataSync) {
        super(ltdata, dataSync);
    }

    public AfLayoutItemViewerAdapter(@LayoutRes int layoutId, List<T> ltdata) {
        super(ltdata);
        mLayoutId = layoutId;
    }

    public AfLayoutItemViewerAdapter(@LayoutRes int layoutId, List<T> ltdata, boolean dataSync) {
        super(ltdata, dataSync);
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
