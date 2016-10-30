package com.andpack.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfView;
import com.andframe.fragment.AfMultiStatusFragment;
import com.andpack.activity.ApFragmentActivity;
import com.andpack.api.ApPager;
import com.andpack.impl.ApStatusHelper;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/21.
 */

public abstract class ApMultiStatusFragment<T> extends AfMultiStatusFragment<T> implements ApPager {

    protected ApStatusHelper mApHelper = new ApStatusHelper(this);

    @Override
    protected void onCreate(AfBundle bundle, AfView view) throws Exception {
        mApHelper.onCreate();
        super.onCreate(bundle, view);
    }

    @Override
    public void onDestroy() {
        mApHelper.onDestroy();
        super.onDestroy();
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
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        ApFragmentActivity.start(clazz, args);
    }

    @Override
    public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
        ApFragmentActivity.startResult(this, clazz, request, args);
    }

}
