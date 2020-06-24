package com.andframe.api.query.handler;

/**
 * 条件选择器
 * Created by SCWANG on 2017/5/11.
 */

public interface FilterIndexed<T> {
    boolean filter(int index ,T model);
}
