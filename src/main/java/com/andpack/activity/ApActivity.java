package com.andpack.activity;

import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.View;

import com.andframe.activity.AfActivity;
import com.andframe.annotation.view.BindAfterViews;
import com.andframe.annotation.view.BindViewModule;
import com.andframe.feature.AfIntent;
import com.andframe.module.AfModuleTitlebar;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

/**
 * 通用页面基类
 * Created by SCWANG on 2016/9/1.
 */
public class ApActivity extends AfActivity implements ApPager {

    @BindViewModule
    protected AfModuleTitlebar mTitlebar;

    protected ApPagerHelper mHelper = new ApPagerHelper(this);

    @Override
    public void setTheme(@StyleRes int resid) {
        mHelper.setTheme(resid);
        super.setTheme(resid);
    }

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
        mHelper.onCreate();
        super.onCreate(bundle, intent);
    }

    @BindAfterViews
    protected void onAfterViews() throws Exception {
        mHelper.onAfterViews(mTitlebar);
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
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        if (mHelper.finish()) {
            return;
        }
        super.finish();
    }

    //<editor-fold desc="下拉刷新">
    @Override
    public boolean onMore() {
        return false;
    }

    @Override
    public boolean onRefresh() {
        return false;
    }
    //</editor-fold>
}
