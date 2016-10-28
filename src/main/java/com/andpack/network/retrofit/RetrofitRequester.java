package com.andpack.network.retrofit;

import android.os.Bundle;
import android.util.Log;

import com.andframe.application.AfApp;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * Retrofit 网络请求封装基类
 * Created by SCWANG on 2016/9/21.
 */
public class RetrofitRequester {

    private static List<Cookie> cookies;

    private static final String TAG = "RetrofitRequester";

    public static ServiceApi service = buildService();

    private static ServiceApi buildService() {
        Interceptor interceptor = chain -> {

            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
//            App app = App.app();
//            if (app.getLoginUser() != null) {
//                builder.addHeader("token", app.getLoginUser().Token);
//            }

            if (AfApp.get().isDebug()) {

                request = builder.build();
                long t1 = System.nanoTime();

                Log.d(TAG, "//请求开始------------------------------------------------------------------");
                Log.d(TAG, "//请求url:" + request.url());
                Headers headers = request.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, "//请求headers:" + headers.name(i) + " = " + headers.value(i));
                }
                Log.d(TAG, "//请求body:" + request.body().toString());
                Log.d(TAG, "//请求结束------------------------------------------------------------------");

                Response response = chain.proceed(request);

                long t2 = System.nanoTime();

                Log.d(TAG, "//服务器返回------------------------------------------------------------------");
                Log.d(TAG, "//返回耗时:" + String.format("%.1fms", (t2 - t1) / 1e6d));
                Log.d(TAG, "//返回StatusCode:" + response.code());
                //Log.d(TAG, "//返回Body:" + response.body().string());
                headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, "//请求headers:" + headers.name(i) + " = " + headers.value(i));
                }
                Log.d(TAG, "//服务器结束------------------------------------------------------------------");

                return response;
            }

            return chain.proceed(builder.build());
        };

        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);
        builder.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                cookies = list;
            }
            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                return cookies == null ? new ArrayList<>() : cookies;
            }
        });

        Bundle data = AfApp.get().getMetaData();
        int port = data.getInt("andrest.port", 4321);
        String ip = data.getString("andrest.host", "192.168.1.207");
        String version = data.getString("andrest.path", "api/android.ashx");

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(CustomConverterFactory.create())
//                .baseUrl(new HttpUrl.Builder().host(ip).port(port).scheme("http").build())
//                .baseUrl("http://192.168.1.207:8080/api/ios.ashx/")
                .baseUrl("http://" + ip + ":" + port + "/" + version + "/")
                .addCallAdapterFactory(new CallAdapter.Factory() {
                    @Override
                    public CallAdapter<?> get(final Type returnType, Annotation[] annotations, Retrofit retrofit) {
                        Class<?> rawType = getRawType(returnType);
                        if (/*rawType == Observable.class || */rawType == Call.class || rawType == Response.class) {
                            return null;
                        }
                        return new CallAdapter<Object>() {
                            public Type responseType() {
                                return returnType;
                            }

                            public <R> Object adapt(Call<R> call) {
                                try {
                                    retrofit2.Response<R> response = call.execute();
                                    if (response.code() != 200) {
                                        throw new RuntimeException("response.code() = " + response.code());
                                    }
                                    return response.body();
                                } catch (IOException e) {
                                    throw new RuntimeException("call.execute", e);
                                }
                            }
                        };
                    }
                })
                .build();

        return retrofit.create(ServiceApi.class);
    }

    public static void clearCookies() {
        cookies = null;
    }
}
