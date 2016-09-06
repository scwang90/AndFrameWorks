package com.andframe.api;

import android.view.ViewGroup;

/**
 * 框架中使用到的常量
 * Created by SCWANG on 2016/9/1.
 */
@SuppressWarnings("unused")
public interface Constanter {

    //<editor-fold desc="页面传值">
    String EXTRA_DATA = "EXTRA_DATA";//通用数据传递标识
    String EXTRA_INDEX = "EXTRA_INDEX";//通用下标栓地标识
    String EXTRA_RESULT = "EXTRA_RESULT";//通用返回传递标识
    String EXTRA_MAIN = "EXTRA_MAIN";//主要数据传递标识
    String EXTRA_DEPUTY = "EXTRA_DEPUTY";//主要数据传递标识
    //</editor-fold>

    //<editor-fold desc="视图常用">
    int LP_MP = ViewGroup.LayoutParams.MATCH_PARENT;
    int LP_WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    //</editor-fold>
}
