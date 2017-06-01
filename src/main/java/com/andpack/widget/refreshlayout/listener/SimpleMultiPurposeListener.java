package com.andpack.widget.refreshlayout.listener;

import com.andpack.widget.refreshlayout.SmartRefreshLayout;
import com.andpack.widget.refreshlayout.api.RefreshFooter;
import com.andpack.widget.refreshlayout.api.RefreshHeader;
import com.andpack.widget.refreshlayout.constant.RefreshState;

/**
 * 多功能监听器
 * Created by SCWANG on 2017/5/26.
 */

public class SimpleMultiPurposeListener implements OnMultiPurposeListener {

    @Override
    public void onHeaderPulling(RefreshHeader header, int offset, int bottomHeight, int extendHeight) {

    }

    @Override
    public void onHeaderReleasing(RefreshHeader header, int offset, int bottomHeight, int extendHeight) {

    }

    @Override
    public void onHeaderStartAnimator(RefreshHeader header, int bottomHeight, int extendHeight) {

    }

    @Override
    public void onHeaderFinish(RefreshHeader header) {

    }

    @Override
    public void onFooterPulling(RefreshFooter footer, int offset, int headHeight, int extendHeight) {

    }

    @Override
    public void onFooterReleasing(RefreshFooter footer, int offset, int headHeight, int extendHeight) {

    }

    @Override
    public void onFooterStartAnimator(RefreshFooter footer, int headHeight, int extendHeight) {

    }

    @Override
    public void onFooterFinish(RefreshFooter footer) {

    }

    @Override
    public void onLoadmore(SmartRefreshLayout refreshlayout) {

    }

    @Override
    public void onRefresh(SmartRefreshLayout refreshlayout) {

    }

    @Override
    public void onStateChanged(RefreshState state) {

    }
}
