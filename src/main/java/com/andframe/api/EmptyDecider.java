package com.andframe.api;

import android.support.annotation.Nullable;

/**
 * 判断是否为空
 * Created by SCWANG on 2016/12/2.
 */

public interface EmptyDecider<T> {

    boolean isEmpty(@Nullable T model);

}
