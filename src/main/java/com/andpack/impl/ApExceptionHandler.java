package com.andpack.impl;

import com.andframe.exception.AfExceptionHandler;
import com.andframe.exception.AfToastException;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

/**
 *
 * Created by SCWANG on 2016/9/6.
 */
public class ApExceptionHandler extends AfExceptionHandler {

    @Override
    public String onHandleTip(Throwable e, CharSequence tip) {
        /*if (e instanceof ApServerException) {
            if (AfApp.get().isDebug()) {
                e = new AfToastException("服务器返回：" + e.getMessage(), e);
            } else {
            }
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            e = new AfToastException("连接服务器失败", e);
        } else if (e instanceof SocketTimeoutException || e instanceof ConnectTimeoutException) {
            e = new AfToastException("网络请求超时", e);
        } else if (e instanceof TimeoutException) {
            e = new AfToastException("请求超时", e);
        }*/

        return super.onHandleTip(e, tip);
    }

}
