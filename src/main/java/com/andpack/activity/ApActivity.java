package com.andpack.activity;

import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.activity.AfActivity;
import com.andframe.annotation.view.BindViewCreated;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

/**
 * 通用页面基类
 * Created by SCWANG on 2016/9/1.
 */
public class ApActivity extends AfActivity implements ApPager {

    protected ApPagerHelper mApHelper = new ApPagerHelper(this);

    @Override
    public void setTheme(@StyleRes int resid) {
        mApHelper.setTheme(resid);
        super.setTheme(resid);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        mApHelper.onCreate();
        super.onCreate(bundle);
    }

    @Override
    protected void onDestroy() {
        mApHelper.onDestroy();
        super.onDestroy();
    }

    @BindViewCreated
    protected void onViewCreated() throws Exception {
        mApHelper.onViewCreated();
    }


    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null)
            return mApHelper.findViewById(id);
        return v;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mApHelper.onPostCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        if (mApHelper.finish()) {
            return;
        }
        super.finish();
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
