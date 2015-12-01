package com.andrestrequest.http.config;

import com.andrestrequest.AndRestConfig;

import java.io.InputStream;
import java.util.Properties;

/**
 * 加载器
 * Created by Administrator on 2015/11/28 0028.
 */
public class Loader {

    public static final String ANDREST_CHARSET = "andrest.charset";

    public static Config load(String path) {
        Config config = new Config();
        InputStream inputStream = AndRestConfig.class.getResourceAsStream(path);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            config.ip = properties.getProperty("andrest.ip");
            config.port = properties.getProperty("andrest.port");
            config.version = properties.getProperty("andrest.version");
            config.charset = properties.getProperty("andrest.charset");
            config.socketTimeout = Integer.valueOf(properties.getProperty("andrest.http.socketTimeout"));
            config.connectionTimeout = Integer.valueOf(properties.getProperty("andrest.http.connectionTimeout"));

            config.status = properties.getProperty("andrest.response.status");
            config.status_ok = properties.getProperty("andrest.response.status_ok");
            config.result = properties.getProperty("andrest.response.result");
            config.successcode = Integer.valueOf(properties.getProperty("andrest.response.successcode"));
            config.jsonframework = Boolean.valueOf(properties.getProperty("andrest.response.jsonframework"));
        } catch (Throwable e) {
            e.printStackTrace();
            // 忽略异常
        }
        return config;
    }
}
