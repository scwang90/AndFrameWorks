package com.andpack.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.activity.AfFragmentActivity;
import com.andframe.annotation.view.BindViewCreated;
import com.andframe.feature.AfIntent;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

/**
 * 通用页面基类
 * Created by SCWANG on 2016/9/1.
 */
@SuppressWarnings("unused")
public class ApFragmentActivity extends AfFragmentActivity implements ApPager {

    protected ApPagerHelper mApHelper = new ApPagerHelper(this);

    static {
        activityClazz = ApFragmentActivity.class;
    }

    @Override
    public void setTheme(@StyleRes int resid) {
        mApHelper.setTheme(resid);
        super.setTheme(resid);
    }

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) {
        mApHelper.onCreate();
        super.onCreate(bundle, intent);
    }

    @BindViewCreated@CallSuper
    protected void onViewCreated() {
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

    //<editor-fold desc="跳转封装">

    public static void start(Class<? extends Fragment> clazz, Object... params) {
        AfFragmentActivity.start(clazz, params);
    }
    public static void startResult(Class<? extends Fragment> clazz,int request, Object... params){
        AfFragmentActivity.startResult(clazz, request, params);
    }
    public static void startResult(Fragment fragment, Class<? extends Fragment> clazz,int request, Object... params){
        AfFragmentActivity.startResult(fragment, clazz, request, params);
    }

    //</editor-fold>

}
