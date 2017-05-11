package com.andframe.api.query.hindler;

/**
 * 条件选择器
 * Created by SCWANG on 2017/5/11.
 */

public interface Where<T> {
    boolean where(T model);
}
