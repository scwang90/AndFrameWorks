package com.andframe.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.andframe.annotation.inject.InjectExtra;
import com.andframe.application.AfApp;
import com.andframe.feature.AfIntent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.id.widget_frame;

/**
 * 抽象通用页面基类
 * Created by Administrator on 2016/3/1.
 */
@SuppressWarnings("unused")
public class AfFragmentActivity extends AfActivity {

    private static final String EXTRA_FRAGMENT = "EXTRA_FRAGMENT";

    public static void startFragment(Class<? extends Fragment> clazz, Object... params){
//        mFragmentClazz = clazz;
        AfActivity activity = AfApp.get().getCurActivity();
        if (activity != null) {
            List<Object> list = new ArrayList<>(Arrays.asList(params));
            list.add(0,clazz.getName());
            list.add(0,EXTRA_FRAGMENT);
            activity.startActivity(AfFragmentActivity.class, list.toArray());
        }
    }

    public static void startFragmentForResult(Class<? extends Fragment> clazz,int request, Object... params){
//        mFragmentClazz = clazz;
        AfActivity activity = AfApp.get().getCurActivity();
        if (activity != null) {
            List<Object> list = new ArrayList<>(Arrays.asList(params));
            list.add(0,clazz.getName());
            list.add(0,EXTRA_FRAGMENT);
            activity.startActivityForResult(AfFragmentActivity.class, request, list.toArray());
        }
    }

    //<editor-fold desc="初始方法">
    @InjectExtra(value = EXTRA_FRAGMENT,remark = "页面信息丢失")
    private String mFragmentClazz = null;

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
        super.onCreate(bundle, intent);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(widget_frame);
        setContentView(frameLayout);
        Fragment fragment = (Fragment)Class.forName(mFragmentClazz).newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(widget_frame, fragment);
        transaction.commit();
    }
    //</editor-fold>


}
