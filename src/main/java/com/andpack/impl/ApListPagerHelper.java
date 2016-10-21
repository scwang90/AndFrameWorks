package com.andpack.impl;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.andframe.api.page.Pager;
import com.andframe.api.view.ItemsViewer;
import com.andframe.impl.viewer.ItemsGridViewWrapper;
import com.andframe.impl.viewer.ItemsListViewWrapper;
import com.andframe.impl.viewer.ItemsRecyclerViewWrapper;
import com.andframe.util.java.AfReflecter;
import com.andpack.annotation.BindItemsViewer;
import com.andpack.api.ApPager;
import com.andpack.fragment.ApListFragment;

/**
 * 页面基类帮助类
 * Created by SCWANG on 2016/9/3.
 */
public class ApListPagerHelper extends ApStatusPagerHelper {

    public ApListPagerHelper(ApPager pager) {
        super(pager);
    }

    public <T> ItemsViewer findItemsViewer(Pager listPager) {
        BindItemsViewer viewer = AfReflecter.getAnnotation(listPager.getClass(), ApListFragment.class, BindItemsViewer.class);
        if (viewer == null) {
            throw new RuntimeException("请为页面添加 BindItemsViewer 注解");
        }
        View view = listPager.findViewById(viewer.value());
        if (view instanceof ListView) {
            return new ItemsListViewWrapper((ListView) view);
        } else if (view instanceof GridView) {
            return new ItemsGridViewWrapper((GridView) view);
        } else if (view instanceof RecyclerView) {
            return new ItemsRecyclerViewWrapper((RecyclerView) view);
        } else {
            throw new RuntimeException("BindItemsViewer 指定View未支持");
        }
    }
}
