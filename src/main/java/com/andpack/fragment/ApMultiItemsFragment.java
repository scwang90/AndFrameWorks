package com.andpack.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfView;
import com.andframe.fragment.AfMultiItemsFragment;
import com.andpack.activity.ApFragmentActivity;
import com.andpack.api.ApPager;
import com.andpack.impl.ApItemsHelper;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public abstract class ApMultiItemsFragment<T> extends AfMultiItemsFragment<T> implements ApPager {

    protected ApItemsHelper mApHelper = new ApItemsHelper(this);

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
    protected void onViewCreated() throws Exception {
        mApHelper.onViewCreated();
        super.onViewCreated();
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
