package com.andpack.network.retrofit;

import com.andframe.exception.AfToastException;
import com.andpack.network.exception.ServerException;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class CustomResponseConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    public CustomResponseConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String body = value.string();

            JSONObject json = new JSONObject(body);

            boolean success = json.optBoolean("success");
            String msg = json.optString("msg", null);

            if (success) {
                if (json.has("data")) {
                    Object data = json.get("data");
                    if (data instanceof JSONObject || data instanceof JSONArray) {
                        body = data.toString();
                    } else {
                        body = gson.toJson(data);
                    }
                } else if (msg != null){
                    //noinspection unchecked
                    return (T)msg;
                } else {
                    return null;
                    //noinspection unchecked
                    //return (T)Boolean.valueOf(true);
                }
                return adapter.fromJson(body);
            } else if (msg != null) {
                throw new ServerException(msg, json);
            } else {
                return adapter.fromJson(body);
            }
        } catch (JSONException e) {
            throw new AfToastException("JSON数据解析异常",e);
        } finally {
            value.close();
        }
    }
}