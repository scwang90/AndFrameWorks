package com.andpack.impl;

import android.content.Context;
import android.view.View;

import com.andframe.api.multistatus.RefreshLayouter;
import com.andpack.api.ApPager;

/**
 * 页面基类帮助类
 * Created by SCWANG on 2016/9/3.
 */
public class ApStatusHelper extends ApPagerHelper {

    public ApStatusHelper(ApPager pager) {
        super(pager);
    }

    public RefreshLayouter createRefreshLayouter(Context context) {
        return null;
    }

    public View findContentView() {
        return null;
    }
}
