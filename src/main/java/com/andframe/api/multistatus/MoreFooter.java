package com.andframe.api.multistatus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 底部加载更多
 * Created by SCWANG on 2016/10/21.
 */

public interface MoreFooter {
    void setOnMoreListener(OnMoreListener listener);
    void setAllLoadFinish(boolean finish);
    void finishLoadMore();
    void onUpdateStatus(View view, int index);
    View onCreateView(Context context, ViewGroup parent);
    View getView();
}
