package com.andadvert.exception;

import com.andadvert.event.AdvertExceptionEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 异常捕捉
 * Created by SCWANG on 2017/3/15.
 */
public class ExceptionHandler {

    public static void handle(Throwable e, String remark) {
        EventBus.getDefault().post(new AdvertExceptionEvent(e, remark));
    }

    public static void handleAttach(Throwable e, String remark) {
        EventBus.getDefault().post(new AdvertExceptionEvent(e, remark));
    }
}
