package com.andframe.api.query.hindler;

/**
 * 循环遍历
 * Created by SCWANG on 2017/5/11.
 */

public interface Foreach<T> {
    void foreach(int index, T model);
}
