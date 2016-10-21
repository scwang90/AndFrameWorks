package com.andframe.api.multistatus;

import com.andframe.api.ListItem;

/**
 * 底部加载更多
 * Created by SCWANG on 2016/10/21.
 */

public interface MoreFooter<T> extends ListItem<T> {
    void setOnMoreListener(OnMoreListener listener);
    void setAllLoadFinish(boolean finish);
    void finishLoadMore();
}
