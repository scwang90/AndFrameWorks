package com.andpack.api;

import com.andframe.api.pager.items.ItemsPager;

/**
 * ApItemsPager
 * Created by SCWANG on 2016/9/7.
 */
public interface ApItemsPager<T> extends ApPager, ItemsPager<T>, ApItemBinder<T> {

//    void initItemEffect();
}
