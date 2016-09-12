package com.andpack.activity;

import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.View;

import com.andframe.activity.AfRefreshListActivityImpl;
import com.andframe.annotation.view.BindViewModule;
import com.andframe.feature.AfIntent;
import com.andframe.module.AfModuleTitlebar;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

/**
 * 滑动关闭
 * Created by SCWANG on 2016/7/11.
 */
public abstract class ApRefreshListActivity<T> extends AfRefreshListActivityImpl<T> implements ApPager {

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

    @Override
    protected void onAfterViews() throws Exception {
        mHelper.onAfterViews(mTitlebar);
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
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        mHelper.onPostCreate(bundle);
    }

    @Override
    public void finish() {
        if (mHelper.finish()) {
            return;
        }
        super.finish();
    }

}
