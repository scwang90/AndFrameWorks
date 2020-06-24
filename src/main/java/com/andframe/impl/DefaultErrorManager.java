package com.andframe.impl;

import com.andframe.api.ErrorManager;
import com.andframe.exception.AfExceptionHandler;

public class DefaultErrorManager implements ErrorManager {

    @Override
    public void handle(Throwable ex, String remark) {
        AfExceptionHandler.handle(ex, remark);
    }

    @Override
    public void handle(String message, String remark) {
        AfExceptionHandler.handle(message, remark);
    }

    @Override
    public String message(Throwable e, String remark) {
        return AfExceptionHandler.tip(e, remark);
    }
}
