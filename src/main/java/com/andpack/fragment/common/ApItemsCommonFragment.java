package com.andpack.fragment.common;

import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.adapter.itemviewer.AfBindItemViewer;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.util.java.AfReflecter;
import com.andpack.R;
import com.andpack.annotation.BindTitle;
import com.andpack.fragment.ApItemsFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 通用列表页面
 * Created by SCWANG on 2017/5/18.
 */
public abstract class ApItemsCommonFragment<T> extends ApItemsFragment<T> {

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        int layoutId = LayoutBinder.getBindLayoutId(this, inflater.getContext());
        if (layoutId > 0) {
            return inflater.inflate(layoutId, container, false);
        }
        return inflater.inflate(R.layout.ap_common_listview, container, false);
    }

    @Override
    @CallSuper
    public void onViewCreated() {
        super.onViewCreated();
        BindTitle title = AfReflecter.getAnnotation(getClass(), ApItemsCommonFragment.class, BindTitle.class);
        if (title != null) {
            $(R.id.toolbar_title).text(title.value());
        }
    }

    protected void setToolbarActionTxt(@StringRes int txtId) {
        setToolbarActionTxt(txtId, null);
    }

    protected void setToolbarActionTxt(@StringRes int txtId, View.OnClickListener listener) {
        $(R.id.toolbar_right_txt).text(txtId).clicked(listener);
    }

    protected void setToolbarActionTxt(CharSequence txt) {
        setToolbarActionTxt(txt, null);
    }

    protected void setToolbarActionTxt(CharSequence txt, View.OnClickListener listener) {
        $(R.id.toolbar_right_txt).text(txt).clicked(listener);
    }

    protected void setToolbarActionImg(@IdRes int imgId) {
        setToolbarActionImg(imgId, null);
    }

    protected void setToolbarActionImg(@IdRes int imgId, View.OnClickListener listener) {
        $(R.id.toolbar_right_img).image(imgId).clicked(listener);
    }

    /**
     * 通用Item实现
     * Created by SCWANG on 2017/3/27.
     */
    public static abstract class ApCommonItemViewer<T> extends AfBindItemViewer<T> {

        protected Set<Integer> ensureSet = new HashSet<>();
        protected boolean ensureLayout = false;
        protected Integer[] mShowIds;

        public ApCommonItemViewer(Integer... ids) {
            super(R.layout.ap_common_listitem);
            mShowIds = ids;
        }

        @Override
        @CallSuper
        public void onViewCreated() {
            super.onViewCreated();
            ensureLayout();
        }

        protected void ensureLayout() {
            showIds(mShowIds);
        }

        protected void showIds(Integer... ids) {
            ensureLayout = true;
            List<Integer> showIds = new ArrayList<>(Arrays.asList(ids));
            $().toRoot().toChildrenTree().foreach((ViewQuery.ViewEacher<View>) v -> $(v).visible(showIds.contains(v.getId())));
            ensureLayout = ids.length > 0;
        }

        protected void hideIds(Integer... ids) {
            ensureLayout = true;
            List<Integer> showIds = new ArrayList<>(Arrays.asList(ids));
            $().toRoot().toChildrenTree().foreach((ViewQuery.ViewEacher<View>) v -> $(v).visible(!showIds.contains(v.getId())));
            ensureLayout = ids.length > 0;
        }

        @Override
        public View findViewById(int id) {
            return super.findViewById(id);
        }

        @Override
        public ViewQuery<? extends ViewQuery> $(Integer id, int... ids) {
            return ensureViews(super.$(id, ids));
        }

        @Override
        public ViewQuery<? extends ViewQuery> $(String idvalue, String... idvalues) {
            return ensureViews(super.$(idvalue, idvalues));
        }

        @Override
        public ViewQuery<? extends ViewQuery> $(Class<? extends View> type) {
            return ensureViews(super.$(type));
        }

        @Override
        public ViewQuery<? extends ViewQuery> $(Class<? extends View>[] types) {
            return ensureViews(super.$(types));
        }

        @Override
        public ViewQuery<? extends ViewQuery> $(View... views) {
            return ensureViews(super.$(views));
        }

        @Override
        public ViewQuery<? extends ViewQuery> $(Collection<View> views) {
            return ensureViews(super.$(views));
        }

        protected ViewQuery<? extends ViewQuery> ensureViews(ViewQuery<? extends ViewQuery> $) {
            if (!ensureLayout) {
                for (View view : $.views()) {
                    ensureView(view);
                }
            }
            return $;
        }

        protected void ensureView(View view) {
            Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(view));
            while (!views.isEmpty()) {
                View cview = views.poll();
                int hash = cview.hashCode();
                if (!ensureSet.contains(hash)) {
                    ensureSet.add(hash);
                    cview.setVisibility(View.VISIBLE);
                    if (view.getParent() instanceof ViewGroup) {
                        ViewGroup parent = (ViewGroup) cview.getParent();
                        if (parent.getVisibility() != View.VISIBLE && parent != mLayout) {
                            views.add(parent);
                        }
                    }
                }
            }
        }
    }

}
