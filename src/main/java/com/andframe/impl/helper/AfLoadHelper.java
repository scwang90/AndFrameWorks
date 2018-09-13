package com.andframe.impl.helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;

import com.andframe.R;
import com.andframe.activity.AfLoadActivity;
import com.andframe.annotation.pager.load.LoadContentViewId;
import com.andframe.annotation.pager.load.LoadContentViewType;
import com.andframe.annotation.pager.load.idname.LoadContentViewId$;
import com.andframe.api.pager.load.LoadHelper;
import com.andframe.api.pager.load.LoadPager;
import com.andframe.api.pager.status.RefreshLayoutManager;
import com.andframe.api.task.Task;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.fragment.AfLoadFragment;
import com.andframe.task.AfHandlerDataTask;
import com.andframe.util.internal.TAG;
import com.andframe.util.java.AfReflecter;

import java.util.Collections;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 加载页面支持
 * Created by SCWANG on 2016/10/22.
 */

public class AfLoadHelper<T> implements LoadHelper<T> {

    protected LoadPager<T> mPager;

    protected RefreshLayoutManager mRefreshLayoutManager;

    protected T mModel;
    protected boolean mIsLoading = false;
    protected boolean mLoadOnViewCreated = true;


    public AfLoadHelper(LoadPager<T> pager) {
        this.mPager = pager;
    }

    @Override
    public void setLoadTaskOnViewCreated(boolean loadOrNot) {
        mLoadOnViewCreated = loadOrNot;
    }

    @Override
    public void setModel(@Nullable T model) {
        mModel = model;
    }

    public void onViewCreated()  {
        View content = mPager.findContentView();
        if (content != null) {
            mRefreshLayoutManager = mPager.initRefreshLayoutManager(content);
        }

        if (mLoadOnViewCreated && mModel == null) {
            mLoadOnViewCreated = false;
            mPager.postTask(new LoadTask());
        } else if (mModel != null) {
            mPager.onTaskSucceed(mModel);
        }
    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void setLastRefreshTime(@NonNull Date time) {
        if (mRefreshLayoutManager != null) {
            mRefreshLayoutManager.setLastRefreshTime(time);
        }
    }

    //<editor-fold desc="初始化布局">
    public View findContentView() {
        Class<?> stop = mPager instanceof Activity ? AfLoadActivity.class : AfLoadFragment.class;
        LoadContentViewId id = AfReflecter.getAnnotation(mPager.getClass(), stop, LoadContentViewId.class);
        if (id != null) {
            return mPager.findViewById(id.value());
        }
        LoadContentViewId$ id$ = AfReflecter.getAnnotation(mPager.getClass(), stop, LoadContentViewId$.class);
        if (id$ != null) {
            Context context = AfApp.get();
            if (context != null) {
                int idv = context.getResources().getIdentifier(id$.value(), "id", context.getPackageName());
                if (idv > 0) {
                    return mPager.findViewById(idv);
                }
            }
        }
        LoadContentViewType type = AfReflecter.getAnnotation(mPager.getClass(), stop, LoadContentViewType.class);
        if (type != null) {
            return AfApp.get().newViewQuery(mPager).query(type.value()).view();
        }

        Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(mPager.getView()));
        do {
            View view = views.poll();
            if (view != null) {
                if (view instanceof AbsListView
                        || view instanceof RecyclerView
                        || view instanceof ScrollView
                        || view instanceof WebView
                        || view instanceof NestedScrollView
                        || view instanceof ViewPager) {
                    return view;
                } else if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    for (int j = 0; j < group.getChildCount(); j++) {
                        views.add(group.getChildAt(j));
                    }
                }
            }
        } while (!views.isEmpty());

        return null;
    }

    public boolean checkContentViewStruct(View content) {
        ViewParent parent = content.getParent();
        if (parent == null) {
            AfExceptionHandler.handle("内容视图（ContentView）没有父视图，刷新布局（RefreshLayoutManager）初始化失败",
                    TAG.TAG(mPager, "AfLoadHelper", "checkContentViewStruct"));
            return false;
        } else if (parent instanceof ViewPager) {
            AfExceptionHandler.handle("内容视图（ContentView）父视图为ViewPager，刷新布局（RefreshLayoutManager）初始化失败，" +
                            "请用其他布局（Layout）作为ContentView的直接父视图，ViewPager的子视图",
                    TAG.TAG(mPager, "AfLoadHelper", "checkContentViewStruct"));
            return false;
        }
        return true;
    }

    public RefreshLayoutManager initRefreshLayoutManager(View content) {
        if (checkContentViewStruct(content)) {
            ViewParent parent = content.getParent();
            if (parent instanceof ViewGroup){
                ViewGroup group = (ViewGroup) parent;

                int i = group.indexOfChild(content);
                group.removeViewAt(i);

                ViewGroup.LayoutParams params = content.getLayoutParams();
                RefreshLayoutManager layoutManager = mPager.newRefreshLayoutManager(content.getContext());
                layoutManager.setContentView(content);
                layoutManager.setOnRefreshListener(mPager);

                group.addView(layoutManager.getLayout(), i, params);

                return layoutManager;
            }
        }
        return null;
    }

    @NonNull
    public RefreshLayoutManager newRefreshLayoutManager(Context context) {
        return AfApp.get().newRefreshManager(context);
    }
    //</editor-fold>

    //<editor-fold desc="数据加载">

    @Override
    public boolean onRefresh() {
        return mPager.postTask(new LoadTask()).status() != Task.Status.canceled;
    }

    @Override
    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    public void onTaskFinish(@NonNull Task task, T data) {
        if (mRefreshLayoutManager != null && mRefreshLayoutManager.isRefreshing()) {
            mRefreshLayoutManager.finishRefresh(task.success());
        }
        if (task.success()) {
            mPager.onTaskSucceed(data);
        } else {
            if (data != null && mModel != data) {
                mModel = data;
                mPager.onTaskLoaded(data);
            }
            mPager.onTaskFailed(task);
        }
    }

    public void onTaskSucceed(T data) {
        mPager.onTaskLoaded(data);
    }

    public void onTaskFailed(@NonNull Task task) {
        if (mModel != null) {
            mPager.onTaskLoaded(mModel);
            mPager.makeToastShort(task.errorToast(AfApp.get().getString(R.string.status_load_fail)));
        } else {
            mPager.showError(task.errorToast(AfApp.get().getString(R.string.status_load_fail)));
        }
    }

    //</editor-fold>

    //<editor-fold desc="页面状态">

    public void showError(@NonNull String error) {
        if (mRefreshLayoutManager != null && mRefreshLayoutManager.isRefreshing()) {
            mRefreshLayoutManager.finishRefresh(false);
        }
        mPager.makeToastShort(error);
    }
    //</editor-fold>

    //<editor-fold desc="任务类">

    protected abstract class AbLoadTask extends AfHandlerDataTask<T> {

        @Override
        protected boolean onPrepare() {
            mIsLoading = true;
            return super.onPrepare();
        }

        @Override
        protected void onHandle(T data) {
            mIsLoading = false;
        }
    }

    protected class LoadTask extends AbLoadTask {

        @Override
        protected void onHandle(T data) {
            super.onHandle(data);
            mPager.onTaskFinish(this, data);
        }
        @Override
        protected T onLoadData() throws Exception {
            return mModel = mPager.onTaskLoading();
        }
    }

    //</editor-fold>
}
