package com.andpack.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.andframe.activity.AfMultiStatusActivity;
import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.feature.AfIntent;
import com.andpack.api.ApPager;
import com.andpack.impl.ApStatusPagerHelper;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/21.
 */

public class ApMultiStatusActivity<T> extends AfMultiStatusActivity<T> implements ApPager {

    protected ApStatusPagerHelper mApHelper = new ApStatusPagerHelper(this);

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) {
        mApHelper.onCreate();
        super.onCreate(bundle, intent);
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
