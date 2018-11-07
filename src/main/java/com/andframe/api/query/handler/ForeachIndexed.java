package com.andframe.api.query.handler;

/**
 * 循环遍历
 * Created by SCWANG on 2017/5/11.
 */

public interface ForeachIndexed<T> {
    void foreach(int index, T model);
}
