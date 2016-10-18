package com.andframe.widget;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.andframe.api.view.ItemsRefreshableViewer;
import com.andframe.impl.viewer.ItemsRecyclerViewWrapper;
import com.andframe.widget.pulltorefresh.AfPullToRefreshBase;

import java.util.Date;

import static android.R.color.holo_blue_light;
import static android.R.color.holo_green_light;
import static android.R.color.holo_orange_light;
import static android.R.color.holo_red_light;

/**
 * 可刷新的 RecyclerView
 * Created by SCWANG on 2016/10/18.
 */

public class AfRecyclerView extends ItemsRecyclerViewWrapper implements ItemsRefreshableViewer<RecyclerView> {


    private SwipeRefreshLayout mSwipeRefreshLayout;

    public AfRecyclerView(RecyclerView recyclerView) {
        super(recyclerView);
        setUp(recyclerView);
    }

    private void setUp(RecyclerView recyclerView) {

        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        }

        ViewGroup parent = ViewGroup.class.cast(recyclerView.getParent());
        int index = parent.indexOfChild(recyclerView);
        parent.removeView(recyclerView);


        mSwipeRefreshLayout = new SwipeRefreshLayout(recyclerView.getContext());
        mSwipeRefreshLayout.addView(recyclerView);
        mSwipeRefreshLayout.setColorSchemeResources(holo_blue_light, holo_red_light, holo_orange_light, holo_green_light);

        parent.addView(mSwipeRefreshLayout, index, recyclerView.getLayoutParams());
    }

    @Override
    public ViewGroup getRefreshableLayout() {
        return mSwipeRefreshLayout;
    }

    @Override
    public void setLastUpdateTime(Date date) {

    }

    @Override
    public void setOnRefreshListener(AfPullToRefreshBase.OnRefreshListener refreshListener) {
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener::onRefresh);
    }

    @Override
    public void finishRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void finishRefreshFail() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void finishLoadMore() {

    }

    @Override
    public void removeMoreView() {

    }

    @Override
    public void addMoreView() {

    }
}
