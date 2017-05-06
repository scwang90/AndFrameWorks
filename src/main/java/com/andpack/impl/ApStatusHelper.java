package com.andpack.impl;

import android.content.Context;

import com.andframe.api.pager.status.StatusLayouter;
import com.andpack.api.ApPager;

/**
 * 页面基类帮助类
 * Created by SCWANG on 2016/9/3.
 */
public class ApStatusHelper extends ApLoadHelper {

    public ApStatusHelper(ApPager pager) {
        super(pager);
    }

    public StatusLayouter newStatusLayouter(Context context) {
        return null;
    }
}
