package com.andframe.api.query.hindler;

/**
 * 条件选择器
 * Created by SCWANG on 2017/5/11.
 */

public interface MapIndex<F,T> {
    T mapIndex(int index ,F model);
}
