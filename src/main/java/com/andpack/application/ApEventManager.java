package com.andpack.application;

import com.andframe.api.event.EventManager;

import org.greenrobot.eventbus.EventBus;

/**
 * 事件管理器
 * Created by SCWANG on 2017/3/22.
 */

public class ApEventManager implements EventManager {

    @Override
    public void post(Object event) {
        EventBus.getDefault().post(event);
    }

    @Override
    public void postSticky(Object event) {
        EventBus.getDefault().postSticky(event);
    }
}
