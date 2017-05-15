package com.andframe.api.task.handler;

/**
 * 判断数据是否为空
 * Created by SCWANG on 2017/5/15.
 */

public interface EmptyJudger<T> {
    boolean isEmpty(T model);
}
