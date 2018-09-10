package com.andpack.impl;

import android.content.Context;
import android.view.View;

import com.andframe.api.pager.status.RefreshManager;
import com.andpack.api.ApPager;

/**
 * 页面基类帮助类
 * Created by SCWANG on 2017/5/5.
 */
public class ApLoadHelper extends ApPagerHelper {

    public ApLoadHelper(ApPager pager) {
        super(pager);
    }

    public RefreshManager newRefreshManager(Context context) {
        return null;
    }

    public View findContentView() {
        return null;
    }
}
