package com.andpack.fragment;

import android.content.Context;
import android.view.View;

import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfView;
import com.andframe.fragment.AfMultiStatusFragment;
import com.andpack.api.ApPager;
import com.andpack.impl.ApStatusPagerHelper;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/21.
 */

public class ApMultiStatusFragment<T> extends AfMultiStatusFragment<T> implements ApPager {

    protected ApStatusPagerHelper mApHelper = new ApStatusPagerHelper(this);

    @Override
    protected void onCreate(AfBundle bundle, AfView view) throws Exception {
        mApHelper.onCreate();
        super.onCreate(bundle, view);
    }

    @Override
    public void onViewCreated() throws Exception {
        mApHelper.onViewCreated();
        super.onViewCreated();
    }

    @Override
    public View findContentView() {
        View view = mApHelper.findContentView();
        if (view != null) {
            return view;
        }
        return super.findContentView();
    }

    @Override
    public RefreshLayouter createRefreshLayouter(Context context) {
        RefreshLayouter layouter = mApHelper.createRefreshLayouter(context);
        if (layouter != null) {
            return layouter;
        }
        return super.createRefreshLayouter(context);
    }

    @Override
    public boolean onMore() {
        return false;
    }
}
