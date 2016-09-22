package com.andpack.fragment;

import com.andframe.annotation.view.BindViewModule;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfView;
import com.andframe.fragment.AfDetailFragment;
import com.andframe.module.AfModuleTitlebar;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

/**
 * 可异步加载的详细页面
 * Created by SCWANG on 2016/9/7.
 */
public class ApDetailFragment<T> extends AfDetailFragment<T> implements ApPager {

    @BindViewModule
    protected AfModuleTitlebar mTitlebar;

    protected ApPagerHelper mHelper = new ApPagerHelper(this);

    @Override
    protected void onCreated(AfView view, AfBundle bundle) throws Exception {
        mHelper.onCreate();
        super.onCreated(view, bundle);
    }

    @Override
    protected void onAfterViews() {
        mHelper.onAfterViews(mTitlebar);
        super.onAfterViews();
    }

}
