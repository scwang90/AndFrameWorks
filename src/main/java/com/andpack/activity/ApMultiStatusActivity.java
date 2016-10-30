package com.andpack.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.activity.AfMultiStatusActivity;
import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.feature.AfIntent;
import com.andpack.api.ApPager;
import com.andpack.impl.ApStatusHelper;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/21.
 */

public abstract class ApMultiStatusActivity<T> extends AfMultiStatusActivity<T> implements ApPager {

    protected ApStatusHelper mApHelper = new ApStatusHelper(this);

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
    protected void onDestroy() {
        mApHelper.onDestroy();
        super.onDestroy();
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
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        ApFragmentActivity.start(clazz, args);
    }

    @Override
    public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
        ApFragmentActivity.startResult(clazz, request, args);
    }
}
