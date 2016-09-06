package com.andframe.api;

/**
 * 类转换器
 * Created by SCWANG on 2016/9/5.
 */
public interface ModelConvertor<From,To> {
    /**
     * 把数据 From 转成 To
     */
    To convert(From from);
}
