package com.andframe.api.query.handler;

/**
 * 对象映射器
 * Created by SCWANG on 2017/5/11.
 */

public interface Map<F,T> {
    T map(F model);
}
