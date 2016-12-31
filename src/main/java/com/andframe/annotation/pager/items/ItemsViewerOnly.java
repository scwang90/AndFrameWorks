package com.andframe.annotation.pager.items;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记页面只使用 ItemsViewer 没有  多状态页面（Status） 也没有 下拉刷新（RefreshLayout）
 * Created by SCWANG on 2016/3/8.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemsViewerOnly {
    @IdRes int value() default 0;
}
