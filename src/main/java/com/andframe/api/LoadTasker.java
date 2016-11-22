package com.andframe.api;

/**
 * 任务
 * Created by SCWANG on 2016/10/13.
 */

public interface LoadTasker<T> {

    T onLoading() throws Exception;

}
