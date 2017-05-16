package com.andframe.api.query.hindler;

import java.util.Collection;

/**
 * 条件选择器
 * Created by SCWANG on 2017/5/11.
 */

public interface FlatMap<F,T> {
    Collection<T> flatMap(F model);
}
