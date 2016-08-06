package com.andrestful.config;

/**
 * 接收类型
 */
public enum AcceptedMediaType {
    json("application/json"), xml("application/xml"), form("application/x-www-form-urlencoded");
    public final String contentType;

    AcceptedMediaType(String contentType) {
        this.contentType = contentType;
    }
}