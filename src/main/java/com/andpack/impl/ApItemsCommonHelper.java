package com.andpack.impl;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.activity.AfActivity;
import com.andframe.adapter.item.BindListItemViewer;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.api.query.ViewQuery;
import com.andframe.util.java.AfReflecter;
import com.andpack.R;
import com.andpack.activity.ApItemsCommonActivity;
import com.andpack.annotation.BindTitle;
import com.andpack.api.ApItemsPager;
import com.andpack.fragment.common.ApItemsCommonFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * 页面基类帮助类
 * Created by SCWANG on 2016/9/3.
 */
public class ApItemsCommonHelper<T> extends ApItemsHelper<T> {

    public ApItemsCommonHelper(ApItemsPager<T> pager) {
        super(pager);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
        int layoutId = LayoutBinder.getBindLayoutId(mItemPager, inflater.getContext());
        if (layoutId > 0) {
            return inflater.inflate(layoutId, container, false);
        }
        return inflater.inflate(R.layout.ap_common_listview, container, false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mItemPager instanceof AfActivity && mItemPager.getView() == null) {
            ((AfActivity) mItemPager).setContentView(R.layout.ap_common_listview);
        }
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        Class<?> stop = mItemPager instanceof Activity ? ApItemsCommonActivity.class : ApItemsCommonFragment.class;
        BindTitle title = AfReflecter.getAnnotation(mItemPager.getClass(), stop, BindTitle.class);
        if (title != null) {
            if (title.value() != 0) {
                mItemPager.getViewQuery().query(R.id.toolbar_title).text(title.value());
            } else {
                mItemPager.getViewQuery().query(R.id.toolbar_title).text(title.title());
            }
        }
    }

    public void setToolbarActionTxt(@StringRes int txtId) {
        setToolbarActionTxt(txtId, null);
    }

    public void setToolbarActionTxt(@StringRes int txtId,@Nullable View.OnClickListener listener) {
        mItemPager.getViewQuery().query(R.id.toolbar_right_txt).text(txtId).clicked(listener);
    }

    public void setToolbarActionTxt(CharSequence txt) {
        setToolbarActionTxt(txt, null);
    }

    public void setToolbarActionTxt(CharSequence txt,@Nullable View.OnClickListener listener) {
        mItemPager.getViewQuery().query(R.id.toolbar_right_txt).text(txt).clicked(listener);
    }

    public void setToolbarActionImg(@IdRes int imgId) {
        setToolbarActionImg(imgId, null);
    }

    public void setToolbarActionImg(@IdRes int imgId, @Nullable View.OnClickListener listener) {
        mItemPager.getViewQuery().query(R.id.toolbar_right_img).image(imgId).clicked(listener);
    }

    /**
     * 通用Item实现
     * Created by SCWANG on 2017/3/27.
     */
    public static abstract class ApCommonItemViewer<T> extends BindListItemViewer<T> {

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
            $().toRoot().toChildrenTree().foreach((ViewQuery.ViewIterator<View>) v -> $(v).visible(showIds.contains(v.getId())));
            ensureLayout = ids.length > 0;
        }

        protected void hideIds(Integer... ids) {
            ensureLayout = true;
            List<Integer> showIds = new ArrayList<>(Arrays.asList(ids));
            $().toRoot().toChildrenTree().foreach((ViewQuery.ViewIterator<View>) v -> $(v).visible(!showIds.contains(v.getId())));
            ensureLayout = ids.length > 0;
        }

        @Override
        public <TT extends View> TT  findViewById(int id) {
            return ensureView(super.findViewById(id));
        }
//
//        @Override
//        public ViewQuery<? extends ViewQuery> $(Integer id, int... ids) {
//            return ensureViews(super.$(id, ids));
//        }
//
//        @Override
//        public ViewQuery<? extends ViewQuery> $(String idValue, String... idValues) {
//            return ensureViews(super.$(idValue, idValues));
//        }
//
//        @Override
//        public ViewQuery<? extends ViewQuery> $(Class<? extends View> type) {
//            return ensureViews(super.$(type));
//        }
//
//        @Override
//        public ViewQuery<? extends ViewQuery> $(Class<? extends View>[] types) {
//            return ensureViews(super.$(types));
//        }
//
//        @Override
//        public ViewQuery<? extends ViewQuery> $(View... views) {
//            return ensureViews(super.$(views));
//        }
//
//        @Override
//        public ViewQuery<? extends ViewQuery> $(Collection<View> views) {
//            return ensureViews(super.$(views));
//        }
//
//        @Override
//        public ViewQuery<? extends ViewQuery> query(Integer id, int... ids) {
//            return ensureViews(super.query(id, ids));
//        }
//
//        @Override
//        public ViewQuery<? extends ViewQuery> query(String idValue, String... idValues) {
//            return ensureViews(super.query(idValue, idValues));
//        }
//
//        @Override
//        public ViewQuery<? extends ViewQuery> query(Class<? extends View> type) {
//            return ensureViews(super.query(type));
//        }
//
//        @Override
//        public ViewQuery<? extends ViewQuery> query(Class<? extends View>[] types) {
//            return ensureViews(super.query(types));
//        }
//
//        @Override
//        public ViewQuery<? extends ViewQuery> with(View... views) {
//            return ensureViews(super.with(views));
//        }
//
//        @Override
//        public ViewQuery<? extends ViewQuery> with(Collection<View> views) {
//            return ensureViews(super.with(views));
//        }
//
//        protected ViewQuery<? extends ViewQuery> ensureViews(ViewQuery<? extends ViewQuery> $) {
//            if (!ensureLayout) {
//                for (View view : $.views()) {
//                    ensureView(view);
//                }
//            }
//            return $;
//        }

        protected <TT extends View> TT ensureView(TT view) {
            Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(view));
            while (!views.isEmpty()) {
                View childView = views.poll();
                int hash = childView.hashCode();
                if (!ensureSet.contains(hash)) {
                    ensureSet.add(hash);
                    childView.setVisibility(View.VISIBLE);
                    if (view.getParent() instanceof ViewGroup) {
                        ViewGroup parent = (ViewGroup) childView.getParent();
                        if (parent.getVisibility() != View.VISIBLE && parent != mLayout) {
                            views.add(parent);
                        }
                    }
                }
            }
            return view;
        }
    }
}
