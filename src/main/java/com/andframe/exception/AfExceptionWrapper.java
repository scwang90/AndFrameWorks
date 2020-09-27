package com.andframe.exception;

import androidx.annotation.NonNull;

public class AfExceptionWrapper extends AfException {

    public AfExceptionWrapper(String remark, @NonNull Throwable throwable) {
        super(remark + ":\n" + throwable.getMessage(), throwable);
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause().getCause();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getCause().getStackTrace();
    }

    @Override
    public String toString() {
        return super.getCause().toString();
    }
}