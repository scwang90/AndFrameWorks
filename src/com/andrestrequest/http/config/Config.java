package com.andrestrequest.http.config;

import com.andrestrequest.http.ErrorMessage;

/**
 * 配置实体类
 * @author 树朾
 * Created by Administrator on 2015/11/28 0028.
 */
public class Config {
    /** 编码 */
    public String charset = "UTF-8";
    /** 服务器接口版本 */
    public String version = "V1.0.0.0";
    /** 服务器IP */
    public String ip = "127.0.0.1";
    /** 服务器端口 */
    public String port = "1234";
    /** 链接超时 */
    public Integer socketTimeout = 3000;
    /** 链接超时 */
    public Integer connectionTimeout = 3000;
    /** 请求类型 */
    public AcceptedMediaType requestMediaType = AcceptedMediaType.json;
    /** 接收类型 */
    public AcceptedMediaType responseMediaType = AcceptedMediaType.json;

    public String status_ok = "true";
    public String status = null;
    public String result = null;
    public String message = null;
    public boolean jsonframework = true;
    public int successcode = 200;

    public Class<? extends ErrorMessage> ErrorMessageClass = ErrorMessage.class;

    /**
     * 接收类型
     */
    public enum AcceptedMediaType {
        json("application/json"), xml("application/xml"),form("application/x-www-form-urlencoded");
        public final String contentType;
        AcceptedMediaType(String contentType) {
            this.contentType = contentType;
        }
    }
}
