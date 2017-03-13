package com.andpack.api;

import com.andframe.api.pager.Pager;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public interface ApPager extends Pager {

    void postEvent(Object event);
}
