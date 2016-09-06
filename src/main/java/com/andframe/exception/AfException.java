package com.andframe.exception;

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

}
