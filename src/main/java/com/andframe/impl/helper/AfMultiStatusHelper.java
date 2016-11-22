package com.andframe.impl.helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.andframe.R;
import com.andframe.activity.AfMultiStatusActivity;
import com.andframe.annotation.multistatus.MultiContentViewId;
import com.andframe.annotation.multistatus.MultiContentViewType;
import com.andframe.annotation.multistatus.MultiStatusEmpty;
import com.andframe.annotation.multistatus.MultiStatusError;
import com.andframe.annotation.multistatus.MultiStatusInvalidNet;
import com.andframe.annotation.multistatus.MultiStatusLayout;
import com.andframe.annotation.multistatus.MultiStatusProgress;
import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.api.multistatus.StatusLayouter;
import com.andframe.api.page.MultiStatusHelper;
import com.andframe.api.page.MultiStatusPager;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.fragment.AfMultiStatusFragment;
import com.andframe.task.AfDispatcher;
import com.andframe.task.AfHandlerDataTask;
import com.andframe.task.AfHandlerTask;
import com.andframe.util.internal.TAG;
import com.andframe.util.java.AfReflecter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/22.
 */

public class AfMultiStatusHelper<T> implements MultiStatusHelper<T> {

    protected MultiStatusPager<T> mPager;

    protected StatusLayouter mStatusLayouter;
    protected RefreshLayouter mRefreshLayouter;

    protected T mModel;
    protected boolean mIsLoading = false;
    protected boolean mLoadOnViewCreated = true;


    public AfMultiStatusHelper(MultiStatusPager<T> pager) {
        this.mPager = pager;
    }

    @Override
    public void setLoadOnViewCreated(boolean loadOrNot) {
        mLoadOnViewCreated = loadOrNot;
    }


    @CallSuper
    public void onViewCreated() throws Exception {
        View content = mPager.findContentView();
        if (content != null) {
            mRefreshLayouter = mPager.initRefreshLayout(content);
            if (mRefreshLayouter != null) {
                mRefreshLayouter.setOnRefreshListener(mPager);
                mStatusLayouter = mPager.initStatusLayout(mRefreshLayouter.getLayout());
            } else {
                mStatusLayouter = mPager.initStatusLayout(content);
            }
            if (mStatusLayouter != null) {
                mStatusLayouter.setOnRefreshListener(mPager);
            }
        }

        if (mLoadOnViewCreated && mModel == null) {
            mLoadOnViewCreated = false;
            mPager.onRefresh();
        } else if (mModel != null) {
            mPager.onTaskFinish(mModel);
        } else {
            mPager.showEmpty();
        }
    }

    //<editor-fold desc="初始化布局">
    public View findContentView() {
        Class<?> stop = mPager instanceof Activity ? AfMultiStatusActivity.class : AfMultiStatusFragment.class;
        MultiContentViewId id = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiContentViewId.class);
        if (id != null) {
            return mPager.findViewById(id.value());
        }
        MultiContentViewType type = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiContentViewType.class);
        if (type != null) {
            return AfApp.get().newViewQuery(mPager).$(type.value()).view();
        }

        Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(mPager.getView()));
        do {
            View view = views.poll();
            if (view != null) {
                if (view instanceof ListView
                        || view instanceof GridView
                        || view instanceof RecyclerView
                        || view instanceof ScrollView
                        || view instanceof WebView) {
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

    public RefreshLayouter initRefreshLayout(View content) {
        ViewParent parent = content.getParent();
        if (parent == null) {
            AfExceptionHandler.handle("内容视图（ContentView）没有父视图，刷新布局（RefreshLayouter）初始化失败",
                    TAG.TAG(mPager, "AfMultiStatusHelper", "initRefreshLayout"));
        } else if (parent instanceof ViewPager) {
            AfExceptionHandler.handle("内容视图（ContentView）父视图为ViewPager，刷新布局（RefreshLayouter）初始化失败，" +
                    "请用其他布局（Layout）作为ContentView的直接父视图，ViewPager的子视图",
                    TAG.TAG(mPager, "AfMultiStatusHelper", "initRefreshLayout"));
        } else if (parent instanceof ViewGroup){
            ViewGroup group = (ViewGroup) parent;

            int i = group.indexOfChild(content);
            group.removeViewAt(i);

            ViewGroup.LayoutParams params = content.getLayoutParams();
            RefreshLayouter layouter = mPager.newRefreshLayouter(content.getContext());
            layouter.setContenView(content);

            group.addView(layouter.getLayout(), i, params);

            return layouter;
        }
        return null;
    }

    public StatusLayouter initStatusLayout(View content) {
        ViewParent parent = content.getParent();
        if (parent == null) {
            if (mRefreshLayouter == null || mRefreshLayouter.getLayout() != content) {
                AfExceptionHandler.handle("内容视图（ContentView）没有父视图，刷新布局（StatusLayouter）初始化失败",
                        TAG.TAG(mPager, "AfMultiStatusHelper", "initStatusLayout"));
            }
        } else if (parent instanceof ViewPager) {
            if (mRefreshLayouter == null || mRefreshLayouter.getLayout() != content) {
                AfExceptionHandler.handle("内容视图（ContentView）父视图为ViewPager，刷新布局（StatusLayouter）初始化失败，" +
                                "请用其他布局（Layout）作为ContentView的直接父视图，ViewPager的子视图",
                        TAG.TAG(mPager, "AfMultiStatusHelper", "initStatusLayout"));
            }
        } else if (parent instanceof ViewGroup){
            ViewGroup group = (ViewGroup) parent;

            int i = group.indexOfChild(content);
            group.removeViewAt(i);

            ViewGroup.LayoutParams params = content.getLayoutParams();
            StatusLayouter layouter = mPager.newStatusLayouter(content.getContext());
            layouter.setContenView(content);

            group.addView(layouter.getLayout(), i, params);

            Class<?> stop = mPager instanceof Activity ? AfMultiStatusActivity.class : AfMultiStatusFragment.class;
            MultiStatusLayout status = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiStatusLayout.class);
            if (status != null) {
                layouter.setEmptyLayout(status.empty(), status.emptyTxtId());
                layouter.setProgressLayout(status.progress(), status.progressTxtId());
                if (status.error() > 0) {
                    layouter.setErrorLayout(status.error(), status.errorTxtId());
                }
                if (status.invalidNet() > 0) {
                    layouter.setInvalidnetLayout(status.invalidNet(), status.invalidNetTxtId());
                }
            } else {
//            MultiStatusEmpty empty = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiStatusEmpty.class);
//            MultiStatusError error = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiStatusError.class);
//            MultiStatusProgress progress = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiStatusProgress.class);
//            MultiStatusInvalidNet invalidNet = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiStatusInvalidNet.class);
                MultiStatusEmpty empty = combineMultiStatusEmpty(mPager.getClass(), stop);
                MultiStatusError error = combineMultiStatusError(mPager.getClass(), stop);
                MultiStatusProgress progress = combineMultiStatusProgress(mPager.getClass(), stop);
                MultiStatusInvalidNet invalidNet = combineMultiStatusInvalidNet(mPager.getClass(), stop);

                if (empty != null) {
                    layouter.setEmptyLayout(empty.value(), empty.txtId(), empty.btnId(), empty.message());
                }
                if (error != null) {
                    layouter.setErrorLayout(error.value(), error.txtId(), error.btnId());
                }
                if (invalidNet != null) {
                    layouter.setInvalidnetLayout(invalidNet.value(), invalidNet.txtId(), invalidNet.btnId());
                }
                if (progress != null) {
                    layouter.setProgressLayout(progress.value(), progress.txtId());
                }
            }
            layouter.autoCompletedLayout();
            return layouter;
        }
        return null;
    }

    public StatusLayouter newStatusLayouter(Context context) {
        return AfApp.get().newStatusLayouter(context);
    }

    public RefreshLayouter newRefreshLayouter(Context context) {
        return AfApp.get().newRefreshLayouter(context);
    }
    //</editor-fold>

    //<editor-fold desc="数据加载">

    @Override
    public boolean onRefresh() {
        return mPager.postTask(new AbStatusTask() {
            @Override
            protected void onHandle(T data) {
                super.onHandle(data);
                if (isFinish()) {
                    mPager.onTaskFinish(data);
                } else {
                    mPager.onTaskFailed(this);
                }
            }
            @Override
            protected T onLoadData() throws Exception {
                AfDispatcher.dispatch(() -> mPager.showProgress());
                return mModel = mPager.onTaskLoading();
            }
        })/*.setListener(task -> mRefreshLayouter.setRefreshComplete())*/.prepare();
    }

    @Override
    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    public boolean isEmpty(T model) {
        if (model instanceof Collection) {
            return ((Collection) model).isEmpty();
        }
        return model == null;
    }

    public void onTaskFinish(T data) {
        if (mPager.isEmpty(data)) {
            mPager.showEmpty();
        } else {
            mPager.showContent();
            mPager.onTaskLoaded(data);
        }
    }

    public void onTaskFailed(AfHandlerTask task) {
        if (mModel != null) {
            mPager.showContent();
            mPager.makeToastShort(task.makeErrorToast(mPager.getContext().getString(R.string.status_load_fail)));
        } else {
            mPager.showError(task.makeErrorToast(mPager.getContext().getString(R.string.status_load_fail)));
        }
    }

//    /**
//     * 任务加载完成
//     * @param data 加载的数据
//     * @return 数据是否为非空，用于框架自动显示空数据页面
//     */
//    public boolean onTaskLoaded(T data) {
//        return data != null;
//    }

//    /**
//     *
//     * 任务加载（异步线程，由框架自动发出执行）
//     * @return 加载的数据
//     * @throws Exception
//     */
//    public T onTaskLoading() throws Exception {
//        Thread.sleep(1000);
//        return null;
//    }
    //</editor-fold>

    //<editor-fold desc="页面状态">
    public void showEmpty() {
        if (mStatusLayouter != null) {
            mStatusLayouter.showEmpty();
        } else if ((mRefreshLayouter == null || !mRefreshLayouter.isRefreshing())) {
            mPager.hideProgressDialog();
        }
    }

    public void showContent() {
        if (mStatusLayouter != null && !mStatusLayouter.isContent()) {
            mStatusLayouter.showContent();
        } else if (mRefreshLayouter != null && mRefreshLayouter.isRefreshing()) {
            mRefreshLayouter.setRefreshComplete();
        } else {
            mPager.hideProgressDialog();
        }
    }

    public void showProgress() {
        if ((mRefreshLayouter == null || !mRefreshLayouter.isRefreshing())) {
            if (mStatusLayouter != null) {
                if (!mStatusLayouter.isProgress())
                    mStatusLayouter.showProgress();
            } else {
                mPager.showProgressDialog(mPager.getContext().getString(R.string.status_loading));
            }
        }
    }

    public void showError(String error) {
        if (mRefreshLayouter != null && mRefreshLayouter.isRefreshing()) {
            mRefreshLayouter.setRefreshComplete();
        } else if (mStatusLayouter == null || !mStatusLayouter.isProgress()) {
            mPager.hideProgressDialog();
        }
        if (mStatusLayouter != null) {
            mStatusLayouter.showError(error);
        } else {
            mPager.makeToastShort(error);
        }
    }

    @Override
    public void showProgress(String progress) {
        if (mStatusLayouter != null) {
            mStatusLayouter.showProgress(progress);
        } else {
            if (!mPager.isProgressDialogShowing()) {
                mPager.showProgressDialog(progress);
            } else {
                mPager.setProgressDialogText(progress);
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="任务类">

    protected abstract class AbStatusTask extends AfHandlerDataTask<T> {
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
    //</editor-fold>

    //<editor-fold desc="Annotation 实现类">

    public <TT extends Annotation> List<TT> getAnnotations(Class<?> type, Class<?> stoptype, Class<TT> annot) {
        List<TT> list = new ArrayList<>();
        while (type != null && !type.equals(stoptype)) {
            if (type.isAnnotationPresent(annot)) {
                list.add(type.getAnnotation(annot));
            }
            type = type.getSuperclass();
        }
        if (type != null && type.equals(stoptype)) {
            if (type.isAnnotationPresent(annot)) {
                list.add(type.getAnnotation(annot));
            }
        }
        Collections.reverse(list);
        return list;
    }

    public MultiStatusEmpty combineMultiStatusEmpty(Class<?> type, Class<?> stoptype) {
        MultiStatusEmptyImpl impl = new MultiStatusEmptyImpl();
        impl.value = R.layout.af_module_nodata;
        impl.txtId = R.id.module_nodata_description;
        List<MultiStatusEmpty> empties = getAnnotations(type, stoptype, MultiStatusEmpty.class);
        for (MultiStatusEmpty tEmpty : empties) {
            impl.combine(tEmpty);
        }
        return impl;
    }

    public MultiStatusError combineMultiStatusError(Class<?> type, Class<?> stoptype) {
        MultiStatusErrorImpl impl = new MultiStatusErrorImpl();
        impl.value = R.layout.af_module_nodata;
        impl.txtId = R.id.module_nodata_description;
        List<MultiStatusError> empties = getAnnotations(type, stoptype, MultiStatusError.class);
        for (MultiStatusError tEmpty : empties) {
            impl.combine(tEmpty);
        }
        return impl;
    }

    public MultiStatusInvalidNet combineMultiStatusInvalidNet(Class<?> type, Class<?> stoptype) {
        MultiStatusInvalidNetImpl impl = new MultiStatusInvalidNetImpl();
        impl.value = R.layout.af_module_nodata;
        impl.txtId = R.id.module_nodata_description;
        impl.message = mPager.getContext().getString(R.string.status_invalidnet);
        List<MultiStatusInvalidNet> empties = getAnnotations(type, stoptype, MultiStatusInvalidNet.class);
        for (MultiStatusInvalidNet tEmpty : empties) {
            impl.combine(tEmpty);
        }
        return impl;
    }

    public MultiStatusProgress combineMultiStatusProgress(Class<?> type, Class<?> stoptype) {
        MultiStatusProgressImpl impl = new MultiStatusProgressImpl();
        impl.value = R.layout.af_module_progress;
        impl.txtId = R.id.module_progress_loadinfo;
        List<MultiStatusProgress> empties = getAnnotations(type, stoptype, MultiStatusProgress.class);
        for (MultiStatusProgress tEmpty : empties) {
            impl.combine(tEmpty);
        }
        return impl;
    }

    private static class MultiStatusBaseImpl {

        public int value;
        public int txtId;
        public int btnId;
        public String message;
        protected Class<? extends Annotation> annotationType;

        public MultiStatusBaseImpl(Class<? extends Annotation> annotationType) {
            this.annotationType = annotationType;
        }

        public int value() {
            return value;
        }
        public int txtId() {
            return txtId;
        }
        public int btnId() {
            return btnId;
        }
        public String message() {
            return message;
        }
        public Class<? extends Annotation> annotationType() {
            return annotationType;
        }
    }

    private static class MultiStatusEmptyImpl extends MultiStatusBaseImpl implements MultiStatusEmpty {

        public MultiStatusEmptyImpl() {
            super(MultiStatusEmpty.class);
        }

//        public MultiStatusEmptyImpl(MultiStatusEmpty annotation) {
//            super(MultiStatusEmpty.class);
//            this.value = annotation.value();
//            this.txtId = annotation.txtId();
//            this.btnId = annotation.btnId();
//            this.message = annotation.message();
//        }

        public void combine(MultiStatusEmpty annotation) {
            this.value = annotation.value() > 0 ? annotation.value() : value;
            this.txtId = annotation.txtId() > 0 ? annotation.txtId() : txtId;
            this.btnId = annotation.btnId() > 0 ? annotation.btnId() : btnId;
            this.message = TextUtils.isEmpty(annotation.message()) ? message : annotation.message();
        }
    }

    private static class MultiStatusErrorImpl extends MultiStatusBaseImpl implements MultiStatusError {

        public MultiStatusErrorImpl() {
            super(MultiStatusError.class);
        }

//        public MultiStatusErrorImpl(MultiStatusError annotation) {
//            super(MultiStatusError.class);
//            this.value = annotation.value();
//            this.txtId = annotation.txtId();
//            this.btnId = annotation.btnId();
//        }

        public void combine(MultiStatusError annotation) {
            this.value = annotation.value() > 0 ? annotation.value() : value;
            this.txtId = annotation.txtId() > 0 ? annotation.txtId() : txtId;
            this.btnId = annotation.btnId() > 0 ? annotation.btnId() : btnId;
        }
    }

    private static class MultiStatusInvalidNetImpl extends MultiStatusBaseImpl implements MultiStatusInvalidNet {

        public MultiStatusInvalidNetImpl() {
            super(MultiStatusInvalidNet.class);
        }

//        public MultiStatusInvalidNetImpl(MultiStatusInvalidNet annotation) {
//            super(MultiStatusInvalidNet.class);
//            this.value = annotation.value();
//            this.txtId = annotation.txtId();
//            this.btnId = annotation.btnId();
//        }

        public void combine(MultiStatusInvalidNet annotation) {
            this.value = annotation.value() > 0 ? annotation.value() : value;
            this.txtId = annotation.txtId() > 0 ? annotation.txtId() : txtId;
            this.btnId = annotation.btnId() > 0 ? annotation.btnId() : btnId;
        }
    }

    private static class MultiStatusProgressImpl extends MultiStatusBaseImpl implements MultiStatusProgress {

        public MultiStatusProgressImpl() {
            super(MultiStatusProgress.class);
        }

//        public MultiStatusProgressImpl(MultiStatusProgress annotation) {
//            super(MultiStatusProgress.class);
//            this.value = annotation.value();
//            this.txtId = annotation.txtId();
//            this.message = annotation.message();
//        }

        public void combine(MultiStatusProgress annotation) {
            this.value = annotation.value() > 0 ? annotation.value() : value;
            this.txtId = annotation.txtId() > 0 ? annotation.txtId() : txtId;
            this.message = TextUtils.isEmpty(annotation.message()) ? message : annotation.message();
        }
    }

    //</editor-fold>
}
