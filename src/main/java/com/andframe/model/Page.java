package com.andframe.model;

import com.andframe.api.Paging;

/**
 * 分页查询类
 * 分页查询开始索引、开始分页等从 0 开始索引
 * @author 树朾
 */
@SuppressWarnings("unused")
public class Page implements Paging {

    public int FirstResult = 0;
    public int MaxResult = 0;

    public Page() {
    }

    /**
     * 构造一个新的分页查询示例索引是从0开始
     *
     * @param max  分页大小 或 追加查询大小
     * @param begin 分页起始页 或 追加起始页
     */
    public Page(int max, int begin) {
        this.FirstResult = begin;
        this.MaxResult = max;
    }

    /**
     * 转换成单页条数
     * @return 返回单页条数
     */
    public int pageSize() {
        return MaxResult;
    }

    /**
     * 转换成当前页码
     * @return （从0开始）
     */
    public int pageIndex() {
        return FirstResult / Math.max(MaxResult,1);
    }

    @Override
    public int pageStart() {
        return FirstResult;
    }

    @Override
    public Paging pageSize(int size) {
        MaxResult = size;
        return this;
    }

    @Override
    public Paging pageIndex(int index) {
        FirstResult = index * MaxResult;
        return this;
    }

    @Override
    public Paging pageStart(int start) {
        FirstResult = start;
        return this;
    }

}
