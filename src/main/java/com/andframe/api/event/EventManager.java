package com.andframe.api.event;

/**
 * 事件管理器
 * Created by SCWANG on 2017/3/16.
 */

public interface EventManager {
    void post(Object event);
    void postSticky(Object event);
    void register(Object handler);
    void unregister(Object subscriber);
}
