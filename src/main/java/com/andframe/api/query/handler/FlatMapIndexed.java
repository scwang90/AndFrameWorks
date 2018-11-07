package com.andframe.api.query.handler;

import java.util.Collection;

/**
 * 条件选择器
 * Created by SCWANG on 2017/5/11.
 */

public interface FlatMapIndexed<F,T> {
    Collection<T> flatMap(int index ,F model);
}
