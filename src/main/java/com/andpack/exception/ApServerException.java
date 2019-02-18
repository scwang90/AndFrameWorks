package com.andpack.exception;

import com.andframe.application.AfApp;
import com.andframe.exception.AfToastException;

/**
 * 服务器异常
 */
public class ApServerException extends AfToastException {

    public ApServerException() {
    }

    public ApServerException(String message) {
        super((AfApp.get().isDebug() ? "服务器返回：" : "@") + message);
    }

    public ApServerException(String message, Throwable cause) {
        super((AfApp.get().isDebug() ? "服务器返回：" : "@") + message, cause);
    }

    public ApServerException(Throwable cause) {
        super(cause);
    }

}
