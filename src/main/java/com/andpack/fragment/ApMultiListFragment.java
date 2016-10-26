package com.andpack.fragment;

import com.andframe.feature.AfBundle;
import com.andframe.feature.AfView;
import com.andframe.fragment.AfMultiListFragment;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public abstract class ApMultiListFragment<T> extends AfMultiListFragment<T> implements ApPager {

    protected ApPagerHelper mApHelper = new ApPagerHelper(this);

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

}
