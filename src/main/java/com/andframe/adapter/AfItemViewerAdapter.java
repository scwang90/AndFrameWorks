package com.andframe.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.view.ViewQuery;
import com.andframe.application.AfApp;
import com.andframe.impl.viewer.ViewerWarpper;

import java.util.List;

/**
 * 自带布局的适配器
 * Created by SCWANG on 2016/9/2.
 */
@SuppressWarnings("unused")
public abstract class AfItemViewerAdapter<T> extends AfListAdapter<T> implements ItemViewer<T> {

    public AfItemViewerAdapter(Context context, List<T> ltdata) {
        super(context, ltdata);
    }

    public AfItemViewerAdapter(Context context, List<T> ltdata, boolean dataSync) {
        super(context, ltdata, dataSync);
    }

    @NonNull
    @Override
    public ItemViewer<T> newItemViewer(int viewType) {
        return this;
    }

    @Override
    public View onCreateView(Context context, ViewGroup parent) {
        return onCreateItemView(context, parent);
    }

    @Override
    public void onBinding(View view, T model, int index) {
        onBinding(AfApp.get().newViewQuery(new ViewerWarpper(view)), model, index);
    }

    /**
     * 子类绑定数据
     * @param $ 视图查询器
     * @param model 数据
     * @param index 索引
     */
    protected abstract void onBinding(ViewQuery<? extends ViewQuery> $, T model, int index);

    /**
     * 创建Item的布局View
     */
    protected abstract View onCreateItemView(Context context, ViewGroup parent);

}
