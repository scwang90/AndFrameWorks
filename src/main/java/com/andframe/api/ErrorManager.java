package com.andframe.api;


public interface ErrorManager {

    void handle(Throwable ex, String remark);
    void handle(String message, String remark);

    String message(Throwable e, String remark);
}
