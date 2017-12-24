package com.andpack.fragment;

import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;

import com.andframe.fragment.AfTabFragment;
import com.andpack.activity.ApFragmentActivity;
import com.andpack.api.ApPager;
import com.andpack.impl.ApPagerHelper;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public class ApFragment extends AfTabFragment implements ApPager {

    protected ApPagerHelper mApHelper = new ApPagerHelper(this);

    @Override
    protected void onCreated() {
        mApHelper.onCreate();
        super.onCreated();
    }

    @CallSuper
    public void onViewCreated()  {
        super.onViewCreated();
        mApHelper.onViewCreated();
    }

    @Override
    public void onDestroy() {
        mApHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        mApHelper.onDestroyView();
        super.onDestroyView();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean startPager(Class clazz, Object... args) {
        if (Fragment.class.isAssignableFrom(clazz)) {
            ApFragmentActivity.start(this, clazz, args);
        } else {
            return super.startPager(clazz, args);
        }
        return true;
    }

    @Override
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        ApFragmentActivity.start(this, clazz, args);
    }

    @Override
    public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
        ApFragmentActivity.startResult(this, clazz, request, args);
    }

    @Override
    public void postEvent(Object event) {
        mApHelper.postEvent(event);
    }
}
