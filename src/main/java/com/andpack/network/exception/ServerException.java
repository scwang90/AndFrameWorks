package com.andpack.network.exception;


import android.support.annotation.NonNull;

import org.json.JSONObject;

/**
 * ServerException
 * Created by SCWANG on 2016/10/28.
 */

public class ServerException extends RuntimeException {

    public final JSONObject json;

    public ServerException(String message, @NonNull JSONObject json) {
        super(message);
        this.json = json;
    }
}
