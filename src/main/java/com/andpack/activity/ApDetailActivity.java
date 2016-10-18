package com.andpack.activity;

import android.os.Bundle;

import com.andframe.activity.AfDetailActivity;
import com.andframe.annotation.view.BindViewModule;
import com.andframe.feature.AfIntent;
import com.andframe.module.AfModuleTitlebar;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

/**
 * 可异步加载的详细页面
 * Created by SCWANG on 2016/9/7.
 */
public class ApDetailActivity<T> extends AfDetailActivity<T> implements ApPager {

    @BindViewModule
    protected AfModuleTitlebar mTitlebar;

    protected ApPagerHelper mHelper = new ApPagerHelper(this);

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
        mHelper.onCreate();
        super.onCreate(bundle, intent);
    }

    @Override
    protected void onAfterViews() throws Exception {
        mHelper.onAfterViews(mTitlebar);
        super.onAfterViews();
    }

}
