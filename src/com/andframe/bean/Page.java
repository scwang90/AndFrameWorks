package com.andframe.bean;

import java.io.Serializable;

/**
 * 分页查询类
 * 分页查询开始索引、开始分页等从 0 开始索引
 * @author Administrator
 */
public class Page implements Serializable{
    public boolean IsASC = true;
    public String Order = "";
    public int FirstResult = 0;
    public int MaxResult = 0;
    public static final int PAGEMODE_PAGING = 1;
    public static final int PAGEMODE_ADDITIAONAL = 2;

    public Page() {
    }

    /**
     * 构造一个新的分页查询示例索引是从0开始
     *
     * @param max  分页大小 或 追加查询大小
     * @param begin 分页起始页 或 追加起始页
     *                   默认 追加：PAGEMODE_ADDITIAONAL
     */
    public Page(int max, int begin) {
        this.FirstResult = begin;
        this.MaxResult = max;
    }

    /**
     * 构造一个新的分页查询示例索引是从0开始
     *
     * @param max  分页大小 或 追加查询大小
     * @param begin 分页起始页 或 追加起始页
     * @param order      排序列
     *                   默认 追加：PAGEMODE_ADDITIAONAL
     */
    public Page(int max, int begin, String order) {
        this.Order = order;
        this.FirstResult = begin;
        this.MaxResult = max;
    }

    /**
     * 构造一个新的分页查询示例
     *
     * @param max  分页大小 或 追加查询大小
     * @param begin 分页起始页 或 追加起始页
     * @param order      排序列
     * @param isAsc      是否升序
     */
    public Page(int max, int begin, String order, boolean isAsc) {
        this.Order = order;
        this.FirstResult = begin;
        this.MaxResult = max;
        this.IsASC = isAsc;
    }

    /**
     * 构造一个新的分页查询示例
     *
     * @param order 排序列
     * @param asc 是否升序
     */
    public Page(String order, String asc) {
        if (order != null && !order.equals("")) {
            this.Order = order;
            this.IsASC = "asc".equals(asc) || "ASC".equals(asc);
        }
    }

    /**
     * 转换成单页条数
     * @return 返回单页条数
     */
    public int getPageSize() {
        return MaxResult;
    }

    /**
     * 转换成当前页码
     * @return （从0开始）
     */
    public int getcurrentPage() {
        return FirstResult / MaxResult;
    }

    public String getOrder() {
        if (Order == null) {
            return "";
        }
        return Order;
    }

    public String getAsc() {
        if (Order == null || Order.equals("")) {
            return "";
        }
        return IsASC ? "asc" : "desc";
    }

    public String toOrderString() {
        // TODO Auto-generated method stub
        if (Order == null || Order.equals("")) {
            return "";
        }
        return " order by " + Order + (IsASC ? " asc" : " desc");
    }
}
