package com.andframe.impl.helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.andframe.R;
import com.andframe.activity.AfStatusActivity;
import com.andframe.annotation.pager.status.StatusContentViewId;
import com.andframe.annotation.pager.status.StatusContentViewType;
import com.andframe.annotation.pager.status.StatusEmpty;
import com.andframe.annotation.pager.status.StatusError;
import com.andframe.annotation.pager.status.StatusInvalidNet;
import com.andframe.annotation.pager.status.StatusLayout;
import com.andframe.annotation.pager.status.StatusProgress;
import com.andframe.annotation.pager.status.idname.StatusContentViewId$;
import com.andframe.api.pager.status.Layouter;
import com.andframe.api.pager.status.RefreshLayouter;
import com.andframe.api.pager.status.StatusHelper;
import com.andframe.api.pager.status.StatusLayouter;
import com.andframe.api.pager.status.StatusPager;
import com.andframe.api.task.Task;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.fragment.AfStatusFragment;
import com.andframe.util.internal.TAG;
import com.andframe.util.java.AfReflecter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/22.
 */

public class AfStatusHelper<T> extends AfLoadHelper<T> implements StatusHelper<T> {

    protected StatusPager<T> mPager;

    protected StatusLayouter mStatusLayouter;

    public AfStatusHelper(StatusPager<T> pager) {
        super(pager);
        this.mPager = pager;
    }

    @CallSuper
    public void onViewCreated()  {
        View content = mPager.findContentView();
        if (content != null) {
            mPager.initRefreshAndStatusLayouter(content);
        }

        if (mLoadOnViewCreated && mModel == null) {
            mLoadOnViewCreated = false;
            if (mPager.postTask(new LoadTask()).status() != Task.Status.canceld) {
                mPager.showProgress();
            }
        } else if (mModel != null) {
            mPager.onTaskSucceed(mModel);
        } else {
            mPager.showEmpty();
        }
    }

    //<editor-fold desc="初始化布局">
    public View findContentView() {

        Class<?> stop = mPager instanceof Activity ? AfStatusActivity.class : AfStatusFragment.class;
        StatusContentViewId id = AfReflecter.getAnnotation(mPager.getClass(), stop, StatusContentViewId.class);
        if (id != null) {
            return mPager.findViewById(id.value());
        }
        StatusContentViewId$ id$ = AfReflecter.getAnnotation(mPager.getClass(), stop, StatusContentViewId$.class);
        if (id$ != null) {
            Context context = AfApp.get();
            if (context != null) {
                int idv = context.getResources().getIdentifier(id$.value(), "id", context.getPackageName());
                if (idv > 0) {
                    return mPager.findViewById(idv);
                }
            }
        }
        StatusContentViewType type = AfReflecter.getAnnotation(mPager.getClass(), stop, StatusContentViewType.class);
        if (type != null) {
            return AfApp.get().newViewQuery(mPager).$(type.value()).view();
        }

        return super.findContentView();
    }

    @Override
    public boolean cheackContentViewStruct(View content) {
        ViewParent parent = content.getParent();
        if (parent == null) {
            AfExceptionHandler.handle("内容视图（ContentView）没有父视图，刷新布局（RefreshLayouter/StatusLayouter）初始化失败",
                    TAG.TAG(mPager, "AfStatusHelper", "cheackContentViewStruct"));
            return false;
        } else if (parent instanceof ViewPager) {
            AfExceptionHandler.handle("内容视图（ContentView）父视图为ViewPager，刷新布局（RefreshLayouter/StatusLayouter）初始化失败，" +
                            "请用其他布局（Layout）作为ContentView的直接父视图，ViewPager的子视图",
                    TAG.TAG(mPager, "AfStatusHelper", "cheackContentViewStruct"));
            return false;
        }
        return true;
    }

    @Override
    public void initRefreshAndStatusLayouter(View content) {
        if (cheackContentViewStruct(content)) {
            mRefreshLayouter = mPager.initRefreshLayout(content);
            mStatusLayouter = mPager.initStatusLayout(content);

            if (mStatusLayouter != null || mRefreshLayouter != null) {
                ViewGroup group = (ViewGroup) content.getParent();
                int i = group.indexOfChild(content);
                group.removeViewAt(i);
                ViewGroup.LayoutParams params = content.getLayoutParams();
                mPager.initRefreshAndStatusLayouterOrder(mRefreshLayouter,mStatusLayouter,content, group, i, params);
            }
        }
    }

    @Override
    public void initRefreshAndStatusLayouterOrder(Layouter refresh, Layouter status, View content, ViewGroup parent, int index, ViewGroup.LayoutParams lp) {
        if (refresh != null && status != null) {
            refresh.setContenView(content);
            status.setContenView(refresh.getLayout());
            parent.addView(status.getLayout(), index, lp);
        } else if (refresh != null) {
            refresh.setContenView(content);
            parent.addView(refresh.getLayout(), index, lp);
        } else if (status != null) {
            status.setContenView(content);
            parent.addView(status.getLayout(), index, lp);
        }
    }

    @Override
    public RefreshLayouter initRefreshLayout(View content) {
        RefreshLayouter layouter = mPager.newRefreshLayouter(content.getContext());
        layouter.setOnRefreshListener(mPager);
        return layouter;
    }

    public StatusLayouter initStatusLayout(View content) {
        StatusLayouter layouter = mPager.newStatusLayouter(content.getContext());
        layouter.setOnRefreshListener(mPager);

        Class<?> stop = mPager instanceof Activity ? AfStatusActivity.class : AfStatusFragment.class;
        StatusLayout status = AfReflecter.getAnnotation(mPager.getClass(), stop, StatusLayout.class);
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
            StatusEmpty empty = combineStatusEmpty(mPager.getClass(), stop);
            StatusError error = combineStatusError(mPager.getClass(), stop);
            StatusProgress progress = combineStatusProgress(mPager.getClass(), stop);
            StatusInvalidNet invalidNet = combineStatusInvalidNet(mPager.getClass(), stop);

            if (empty != null) {
                String message = empty.message();
                if (TextUtils.isEmpty(message) && empty.messageId() > 0) {
                    message = AfApp.get().getString(empty.messageId());
                }
                layouter.setEmptyLayout(empty.value(), empty.txtId(), empty.btnId(), message);
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

    @NonNull
    public StatusLayouter newStatusLayouter(Context context) {
        return AfApp.get().newStatusLayouter(context);
    }
    //</editor-fold>

    //<editor-fold desc="数据加载">

    @Override
    public boolean isEmpty(T model) {
        if (model instanceof Collection) {
            return ((Collection) model).isEmpty();
        }
        return model == null;
    }

    @Override
    public void onTaskFinish(@NonNull Task task, T data) {
        if (task.isFinish()) {
            mPager.onTaskSucceed(data);
        } else {
            mPager.onTaskFailed(task);
        }
    }

    public void onTaskSucceed(T data) {
        if (mPager.isEmpty(data)) {
            mPager.showEmpty();
        } else {
            mPager.showContent();
            mPager.onTaskLoaded(data);
        }
    }

    public void onTaskFailed(@NonNull Task task) {
        if (mModel != null) {
            mPager.showContent();
            mPager.makeToastShort(task.makeErrorToast(AfApp.get().getString(R.string.status_load_fail)));
        } else {
            mPager.showError(task.makeErrorToast(AfApp.get().getString(R.string.status_load_fail)));
        }
    }

    //</editor-fold>

    //<editor-fold desc="页面状态">
    public void showEmpty() {
        if (mStatusLayouter != null && !mStatusLayouter.isEmpty()) {
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
                mPager.showProgressDialog(AfApp.get().getString(R.string.status_loading));
            }
        }
    }

    public void showError(@NonNull String error) {
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
    public void showProgress(@NonNull String progress) {
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

    public StatusEmpty combineStatusEmpty(Class<?> type, Class<?> stoptype) {
        StatusEmptyImpl impl = new StatusEmptyImpl();
        impl.value = R.layout.af_module_nodata;
        impl.txtId = R.id.module_nodata_description;
        List<StatusEmpty> empties = getAnnotations(type, stoptype, StatusEmpty.class);
        for (StatusEmpty tEmpty : empties) {
            impl.combine(tEmpty);
        }
        return impl;
    }

    public StatusError combineStatusError(Class<?> type, Class<?> stoptype) {
        StatusErrorImpl impl = new StatusErrorImpl();
        impl.value = R.layout.af_module_nodata;
        impl.txtId = R.id.module_nodata_description;
        List<StatusError> empties = getAnnotations(type, stoptype, StatusError.class);
        for (StatusError tEmpty : empties) {
            impl.combine(tEmpty);
        }
        return impl;
    }

    public StatusInvalidNet combineStatusInvalidNet(Class<?> type, Class<?> stoptype) {
        StatusInvalidNetImpl impl = new StatusInvalidNetImpl();
        impl.value = R.layout.af_module_nodata;
        impl.txtId = R.id.module_nodata_description;
        impl.message = AfApp.get().getString(R.string.status_invalidnet);
        List<StatusInvalidNet> empties = getAnnotations(type, stoptype, StatusInvalidNet.class);
        for (StatusInvalidNet tEmpty : empties) {
            impl.combine(tEmpty);
        }
        return impl;
    }

    public StatusProgress combineStatusProgress(Class<?> type, Class<?> stoptype) {
        StatusProgressImpl impl = new StatusProgressImpl();
        impl.value = R.layout.af_module_progress;
        impl.txtId = R.id.module_progress_loadinfo;
        List<StatusProgress> empties = getAnnotations(type, stoptype, StatusProgress.class);
        for (StatusProgress tEmpty : empties) {
            impl.combine(tEmpty);
        }
        return impl;
    }

    private static class StatusBaseImpl {

        public int value;
        public int txtId;
        public int btnId;
        public int messageId;
        public String message;
        protected Class<? extends Annotation> annotationType;

        public StatusBaseImpl(Class<? extends Annotation> annotationType) {
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
        public int messageId() {
            return messageId;
        }
        public String message() {
            return message;
        }
        public Class<? extends Annotation> annotationType() {
            return annotationType;
        }
    }

    private static class StatusEmptyImpl extends StatusBaseImpl implements StatusEmpty {

        public StatusEmptyImpl() {
            super(StatusEmpty.class);
        }

//        public StatusEmptyImpl(StatusEmpty annotation) {
//            super(StatusEmpty.class);
//            this.value = annotation.value();
//            this.txtId = annotation.txtId();
//            this.btnId = annotation.btnId();
//            this.message = annotation.message();
//        }

        public void combine(StatusEmpty annotation) {
            this.value = annotation.value() > 0 ? annotation.value() : value;
            this.txtId = annotation.txtId() > 0 ? annotation.txtId() : txtId;
            this.btnId = annotation.btnId() > 0 ? annotation.btnId() : btnId;
            this.messageId = annotation.messageId() > 0 ? annotation.messageId() : messageId;
            this.message = TextUtils.isEmpty(annotation.message()) ? message : annotation.message();
        }
    }

    private static class StatusErrorImpl extends StatusBaseImpl implements StatusError {

        public StatusErrorImpl() {
            super(StatusError.class);
        }

//        public StatusErrorImpl(StatusError annotation) {
//            super(StatusError.class);
//            this.value = annotation.value();
//            this.txtId = annotation.txtId();
//            this.btnId = annotation.btnId();
//        }

        public void combine(StatusError annotation) {
            this.value = annotation.value() > 0 ? annotation.value() : value;
            this.txtId = annotation.txtId() > 0 ? annotation.txtId() : txtId;
            this.btnId = annotation.btnId() > 0 ? annotation.btnId() : btnId;
        }
    }

    private static class StatusInvalidNetImpl extends StatusBaseImpl implements StatusInvalidNet {

        public StatusInvalidNetImpl() {
            super(StatusInvalidNet.class);
        }

//        public StatusInvalidNetImpl(StatusInvalidNet annotation) {
//            super(StatusInvalidNet.class);
//            this.value = annotation.value();
//            this.txtId = annotation.txtId();
//            this.btnId = annotation.btnId();
//        }

        public void combine(StatusInvalidNet annotation) {
            this.value = annotation.value() > 0 ? annotation.value() : value;
            this.txtId = annotation.txtId() > 0 ? annotation.txtId() : txtId;
            this.btnId = annotation.btnId() > 0 ? annotation.btnId() : btnId;
        }
    }

    private static class StatusProgressImpl extends StatusBaseImpl implements StatusProgress {

        public StatusProgressImpl() {
            super(StatusProgress.class);
        }

//        public StatusProgressImpl(StatusProgress annotation) {
//            super(StatusProgress.class);
//            this.value = annotation.value();
//            this.txtId = annotation.txtId();
//            this.message = annotation.message();
//        }

        public void combine(StatusProgress annotation) {
            this.value = annotation.value() > 0 ? annotation.value() : value;
            this.txtId = annotation.txtId() > 0 ? annotation.txtId() : txtId;
            this.message = TextUtils.isEmpty(annotation.message()) ? message : annotation.message();
        }
    }

    //</editor-fold>
}
