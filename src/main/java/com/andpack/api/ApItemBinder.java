package com.andpack.api;

import com.andframe.api.query.ViewQuery;

/**
 * ListItem数据绑定器
 * Created by SCWANG on 2016/11/10.
 */

public interface ApItemBinder<T> {
    /**
     * 子类绑定数据
     * @param $ 视图查询器
     * @param model 数据
     * @param index 索引
     */
    void onItemBinding(ViewQuery<? extends ViewQuery<?>> $, T model, int index);
}
