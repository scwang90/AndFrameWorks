package com.andframe.api.pager.status;

/**
 * 刷新接口
 * Created by SCWANG on 2016/10/21.
 */
public interface OnRefreshListener {
    /**
     * 用户触发刷新
     * @return 返回false标识将不会执行刷新操作
     */
    boolean onRefresh();
}
