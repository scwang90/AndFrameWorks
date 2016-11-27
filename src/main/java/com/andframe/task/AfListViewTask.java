package com.andframe.task;

/**
 * 各种数据加载任务handler分流
 */
public abstract class AfListViewTask extends AfHandlerTask {
    // 加载单页条数
    public static int PAGE_SIZE = 15;
    //缓存有效时间（5分钟）
    public static int CACHETIMEOUTSECOND = 5 * 60 * 1000;

}
