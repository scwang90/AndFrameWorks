package com.andrestful.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * 加载器
 * Created by Administrator on 2015/11/28 0028.
 */
public class Loader {

    public static final String ANDREST_CHARSET = "reftful.charset";

    public static Config load(String path) {
        Config config = new Config();
        InputStream inputStream = Loader.class.getResourceAsStream(path);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            config.ip = properties.getProperty("reftful.ip");
            config.port = properties.getProperty("reftful.port");
            config.version = properties.getProperty("reftful.version");
            config.charset = properties.getProperty("reftful.charset");
            config.readTimeout = Integer.valueOf(properties.getProperty("reftful.http.readTimeout"));
            config.connectionTimeout = Integer.valueOf(properties.getProperty("reftful.http.connectionTimeout"));

            config.status = properties.getProperty("reftful.response.status");
            config.status_ok = properties.getProperty("reftful.response.status_ok");
            config.result = properties.getProperty("reftful.response.result");
            config.message = properties.getProperty("reftful.response.message");
            config.successcode = Integer.valueOf(properties.getProperty("reftful.response.successcode"));
            config.jsonframework = Boolean.valueOf(properties.getProperty("reftful.response.jsonframework"));
        } catch (Throwable e) {
            e.printStackTrace();
            // 忽略异常
        }
        return config;
    }
}
