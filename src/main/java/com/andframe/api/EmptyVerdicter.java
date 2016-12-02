package com.andframe.api;

/**
 * 判断是否为空
 * Created by SCWANG on 2016/12/2.
 */

public interface EmptyVerdicter<T> {

    boolean isEmpty(T model);

}
