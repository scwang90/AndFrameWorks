package com.andframe.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.query.ViewQuery;
import com.andframe.application.AfApp;
import com.andframe.impl.helper.ViewQueryHelper;
import com.andframe.impl.viewer.ViewerWrapper;

import java.util.Collection;
import java.util.List;

/**
 * 自带布局的适配器
 * Created by SCWANG on 2016/9/2.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class DirectItemViewerAdapter<T> extends ListItemAdapter<T> implements ItemViewer<T>, com.andframe.api.query.ViewQueryHelper {

    public static int TAG_VIEW_QUERY = "DirectItemViewerAdapter.TAG_VIEW_QUERY".hashCode();

    public DirectItemViewerAdapter(List<T> list) {
        super(list);
    }

    public DirectItemViewerAdapter(List<T> list, boolean dataSync) {
        super(list, dataSync);
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
        ViewQuery<? extends ViewQuery> query;
        Object tagObject = view.getTag(TAG_VIEW_QUERY);
        if (tagObject instanceof ViewQuery) {
            query = (ViewQuery<? extends ViewQuery>) tagObject;
        } else {
            query = ViewQueryHelper.newHelper(new ViewerWrapper(view));
            view.setTag(TAG_VIEW_QUERY, query);
        }
        $$ = query;
        onBinding(model, index);
    }

    /**
     * 子类绑定数据
     * @param $ 视图查询器
     * @param model 数据
     * @param index 索引
     */
    protected abstract void onBinding(T model, int index);

    /**
     * 创建Item的布局View
     */
    protected abstract View onCreateItemView(Context context, ViewGroup parent);


    //<editor-fold desc="ViewQuery 集成">

    protected ViewQuery<? extends ViewQuery> $$ = ViewQueryHelper.newHelper(new ViewerWrapper(new View(AfApp.get())));

    @Override
    public ViewQuery<? extends ViewQuery> $(View... views) {
        return $$.with(views);
    }

    @Override
    public ViewQuery<? extends ViewQuery> $(Collection<View> views) {
        return $$.with(views);
    }

    @Override
    public ViewQuery<? extends ViewQuery> $(Integer id, int... ids) {
        return $$.query(id, ids);
    }

    @Override
    public ViewQuery<? extends ViewQuery> $(String idValue, String... idValues) {
        return $$.query(idValue);
    }

    @Override
    public ViewQuery<? extends ViewQuery> $(Class<? extends View> type) {
        return $$.query(type);
    }

    @Override
    public ViewQuery<? extends ViewQuery> $(Class<? extends View>[] types) {
        return $$.query(types);
    }

    @Override
    public ViewQuery<? extends ViewQuery> with(View... views) {
        return $$.with(views);
    }

    @Override
    public ViewQuery<? extends ViewQuery> with(Collection<View> views) {
        return $$.with(views);
    }

    @Override
    public ViewQuery<? extends ViewQuery> query(Integer id, int... ids) {
        return $$.query(id, ids);
    }

    @Override
    public ViewQuery<? extends ViewQuery> query(String idValue, String... idValues) {
        return $$.query(idValue);
    }

    @Override
    public ViewQuery<? extends ViewQuery> query(Class<? extends View> type) {
        return $$.query(type);
    }

    @Override
    public ViewQuery<? extends ViewQuery> query(Class<? extends View>[] types) {
        return $$.query(types);
    }
    //</editor-fold>

}
