package com.andpack.activity;

import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.activity.AfActivity;
import com.andframe.activity.AfFragmentActivity;
import com.andframe.application.AfApp;
import com.andframe.feature.AfIntent;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用页面基类
 * Created by SCWANG on 2016/9/1.
 */
public class ApFragmentActivity extends AfFragmentActivity implements ApPager {

    protected ApPagerHelper mHelper = new ApPagerHelper(this);

    static {
        activityClazz = ApFragmentActivity.class;
    }

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


    //<editor-fold desc="FragmentActivity启动">
    public static void startFragment(Class<? extends Fragment> clazz, Object... params){
        AfActivity activity = AfApp.get().getCurActivity();
        if (activity != null) {
            List<Object> list = new ArrayList<>(Arrays.asList(params));
            list.add(0,clazz.getName());
            list.add(0,EXTRA_FRAGMENT);
            activity.startActivity(ApFragmentActivity.class, list.toArray());
        }
    }
    public static void startFragmentForResult(Class<? extends Fragment> clazz,int request, Object... params){
        AfActivity activity = AfApp.get().getCurActivity();
        if (activity != null) {
            List<Object> list = new ArrayList<>(Arrays.asList(params));
            list.add(0,clazz.getName());
            list.add(0,EXTRA_FRAGMENT);
            activity.startActivityForResult(activityClazz, request, list.toArray());
        }
    }
    //</editor-fold>
}
