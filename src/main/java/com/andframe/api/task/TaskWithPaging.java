package com.andframe.api.task;

import android.support.annotation.Nullable;

import com.andframe.api.Paging;

/**
 * 带有分页的任务
 * Created by SCWANG on 2016/10/13.
 */

public interface TaskWithPaging extends TaskWithHandler {
    /**
     * 分页对象
     * @return （null表示不使用分页功能）
     */
    @Nullable
    Paging getPaging();

}
