package com.andframe.api.pager.items;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 底部加载更多
 * Created by SCWANG on 2016/10/21.
 */

public interface MoreFooter extends MoreLayoutManager {
    void onUpdateStatus(View view, int index);
    View onCreateView(Context context, ViewGroup parent);
    View getView();

    /**
     * MoreFooter 被设定为放在 ItemsView 的末尾
     * 所以 MoreFooter 除了点击触发 LoadMore 以外
     * ItemsView 滚动到底部时 通过调用 triggerLoadMore 也可以触发 LoadMore
     */
    void triggerLoadMore();
}
