package com.andframe.api.pager.items;

/**
 * 加载更多布局
 * Created by SCWANG on 2016/10/21.
 */

public interface MoreLayoutManager {
    void setOnMoreListener(OnMoreListener listener);
    void setLoadMoreEnabled(boolean enable);
    void finishLoadMore();
}
