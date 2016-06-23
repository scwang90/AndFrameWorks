package com.andframe.exception;

import com.andframe.application.AfApplication;

public class AfException extends RuntimeException {

    private static final long serialVersionUID = -2299984118501502745L;

    public AfException() {
        super();
    }

    public AfException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AfException(String detailMessage) {
        super(detailMessage);
    }

    public AfException(Throwable throwable) {
        super(throwable);
    }

    public static String handle(Throwable e, String tip) {
        int index = 0;
        Throwable ex = e;
        while (!(ex instanceof AfToastException) && ex.getCause() != null && ++index < 5) {
            ex = e.getCause();
        }
        if (ex instanceof AfToastException) {
            return ex.getMessage();
        }
        String message = e.getMessage();
        boolean debug = AfApplication.getApp().isDebug();
        if (debug) {
            message = "内容:" + message;
            message = String.format("异常:%s\r\n%s", e.getClass().getName(), message);
            if (tip != null && !tip.equals("")) {
                return String.format("消息:%s\r\n%s", tip, message);
            }
        } else {
            if (message == null || message.trim().equals("")) {
                message = e.getClass().getName();
            }
            if (tip != null && !tip.equals("")) {
                return tip;
//				return String.format("%s:%s",tip,message);
            }
        }
        return message;
    }


}
