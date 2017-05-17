package com.andframe.api;

/**
 * 分页模型接口
 * Created by SCWANG on 2017/1/6.
 */

public interface Paging {

    /**
     * 单页条数
     * @return 返回单页条数
     */
    int pageSize();

    /**
     * 转换成当前页码（与 pageStart 对应）
     * @return （从0开始）
     */
    int pageIndex();

    /**
     * 获取分页开始条数（与 pageIndex 对应）
     * @return （从0开始）
     */
    int pageStart();

    /**
     * 设置单页条数
     */
    Paging pageSize(int size);

    /**
     * 设置开始条数（与 setPageIndex 对应）
     */
    Paging pageStart(int start);

    /**
     * 设置当前页数（与 setPageStart 对应）
     */
    Paging pageIndex(int index);

    /**
     * 获取排序字段
     */
    String order();
    /**
     * 获取排序字段
     */
    Paging order(String order);

    /**
     * 排序方向
     */
    boolean asc();

    /**
     * 设置排序方向
     */
    Paging asc(boolean asc);
}
