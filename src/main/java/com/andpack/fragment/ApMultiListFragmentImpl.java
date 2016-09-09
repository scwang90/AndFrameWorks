package com.andpack.fragment;

import com.andframe.annotation.view.BindViewModule;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfView;
import com.andframe.fragment.AfMultiListFragmentImpl;
import com.andframe.module.AfModuleTitlebar;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public abstract class ApMultiListFragmentImpl<T> extends AfMultiListFragmentImpl<T> implements ApPager {

    @BindViewModule
    protected AfModuleTitlebar mTitlebar;

    protected ApPagerHelper mHelper = new ApPagerHelper(this);

    @Override
    protected void onCreated(AfBundle bundle, AfView view) throws Exception {
        mHelper.onCreate(mTitlebar);
        super.onCreated(bundle, view);
    }

    @Override
    public void onAfterViews() throws Exception {
        mHelper.onAfterViews();
        super.onAfterViews();
    }

}
