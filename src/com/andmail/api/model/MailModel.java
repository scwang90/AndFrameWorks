package com.andmail.api.model;

/**
 * 邮件基本数据模型
 * Created by SCWANG on 2017/1/10.
 */

public interface MailModel {

    String getHost();

    void setHost(String host);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);
}
