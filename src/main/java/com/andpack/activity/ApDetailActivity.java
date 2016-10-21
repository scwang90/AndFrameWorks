package com.andpack.activity;

import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.View;

import com.andframe.activity.AfDetailActivity;
import com.andframe.annotation.view.BindViewModule;
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
    public void setTheme(@StyleRes int resid) {
        mHelper.setTheme(resid);
        super.setTheme(resid);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        mHelper.onCreate();
        super.onCreate(bundle);
    }

    @Override
    protected void onAfterViews() throws Exception {
        mHelper.onViewCreated(mTitlebar);
        super.onAfterViews();
    }


    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        mHelper.onPostCreate(savedInstanceState);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        if (mHelper.finish()) {
            return;
        }
        super.finish();
    }

}
