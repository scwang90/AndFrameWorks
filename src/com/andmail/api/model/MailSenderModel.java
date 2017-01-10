package com.andmail.api.model;

/**
 * 邮件基本数据模型
 * Created by SCWANG on 2017/1/10.
 */

public interface MailSenderModel extends MailModel{

    void setSendto(String sendto);

    String getSendto();

    void setFrom(String from);

    String getFrom();
}
