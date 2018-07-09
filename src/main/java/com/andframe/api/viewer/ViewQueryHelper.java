package com.andframe.api.viewer;

import android.view.View;

import java.util.Collection;

/**
 * ViewQuery 继承帮助类 接口
 * Created by SCWANG on 2016/9/8.
 */
public interface ViewQueryHelper {

    /**
     * 开始 ViewQuery 查询
     * @param id 控件Id
     */
    ViewQuery<? extends ViewQuery> $(Integer id, int... ids);
    /**
     * 开始 ViewQuery 查询
     * @param views 可选的多个 View
     */
    ViewQuery<? extends ViewQuery> $(View... views);
    ViewQuery<? extends ViewQuery> $(Collection<View> views);

    ViewQuery<? extends ViewQuery> $(String idValue, String... idValues);
    ViewQuery<? extends ViewQuery> $(Class<? extends View> type);
    ViewQuery<? extends ViewQuery> $(Class<? extends View>[] types);

    /**
     * 开始 ViewQuery 查询
     * @param id 控件Id
     */
    ViewQuery<? extends ViewQuery> query(Integer id, int... ids);
    ViewQuery<? extends ViewQuery> query(String idValue, String... idValues);

    ViewQuery<? extends ViewQuery> query(Class<? extends View> type);
    ViewQuery<? extends ViewQuery> query(Class<? extends View>[] types);
    /**
     * 开始 ViewQuery 查询
     * @param views 可选的多个 View
     */
    ViewQuery<? extends ViewQuery> with(View... views);
    ViewQuery<? extends ViewQuery> with(Collection<View> views);
}
