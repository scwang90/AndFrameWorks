package com.andframe.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListAdapter;

import com.andframe.adapter.AfContactsAdapter;
import com.andframe.api.Viewer;
import com.andframe.widget.pulltorefresh.PullRefreshFooterImpl;
import com.andframe.widget.pulltorefresh.PullRefreshHeaderImpl;

@SuppressWarnings("unused")
public class AfContactsRefreshView extends AfRefreshAbsListView<AfContactsListView> {

    private static AfContactsListView mlistView = null;

    public AfContactsRefreshView(AfContactsListView listView) {
        super((mlistView = listView).getContext());
        setPullFooterLayout(new PullRefreshFooterImpl(listView.getContext()));
        setPullHeaderLayout(new PullRefreshHeaderImpl(listView.getContext()));
    }

    public AfContactsRefreshView(Viewer viewable, int res) {
        super((mlistView = viewable.findViewByID(res)).getContext());
        setPullFooterLayout(new PullRefreshFooterImpl(viewable.getContext()));
        setPullHeaderLayout(new PullRefreshHeaderImpl(viewable.getContext()));
    }

    public AfContactsRefreshView(Context context) {
        super(context);
        setPullFooterLayout(new PullRefreshFooterImpl(context));
        setPullHeaderLayout(new PullRefreshHeaderImpl(context));
    }

    public AfContactsRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPullFooterLayout(new PullRefreshFooterImpl(context));
        setPullHeaderLayout(new PullRefreshHeaderImpl(context));
    }

    public AfContactsRefreshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPullFooterLayout(new PullRefreshFooterImpl(context));
        setPullHeaderLayout(new PullRefreshHeaderImpl(context));
    }

    @Override
    protected AfContactsListView onCreateTargetView(Context context, AttributeSet attrs) {
        if (mlistView != null) {
            if (getParent() == null && mlistView.getParent() instanceof ViewGroup) {
                ViewGroup parent = ViewGroup.class.cast(mlistView.getParent());
                int index = parent.indexOfChild(mlistView);
                parent.removeView(mlistView);
                parent.addView(this, index, mlistView.getLayoutParams());
                mTargetView = mlistView;
                mlistView = null;
            }
            return mTargetView;
        }
        return new AfContactsListView(context);
    }

    @Override
    protected AfContactsListView onCreateRefreshableView(Context context, AttributeSet attrs) {
        mAbsListView = onCreateTargetView(context, attrs);//new ListView(context)
        // 解决listview在拖动的时候背景图片消失变成黑色背景
        mAbsListView.setCacheColorHint(0);
        mAbsListView.setScrollingCacheEnabled(false);
        // 解决listview的上边和下边有黑色的阴影
        mAbsListView.setFadingEdgeLength(0);
        return mAbsListView;
    }

    public void setHeaderView(View view) {
        mAbsListView.setHeaderView(view);
    }

    @Override
    @Deprecated
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        throw new RuntimeException("请使用 setAdapter(AfContactsAdapter adapter)");
    }

    @SuppressWarnings("rawtypes")
    public void setAdapter(AfContactsAdapter adapter) {
        mAbsListView.setAdapter(adapter);
    }

    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        mAbsListView.setOnChildClickListener(onChildClickListener);
    }
}
