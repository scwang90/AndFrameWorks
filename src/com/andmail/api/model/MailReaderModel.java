package com.andmail.api.model;

/**
 * 邮件基本数据模型
 * Created by SCWANG on 2017/1/10.
 */

public interface MailReaderModel extends MailModel{

    String getFolder() ;

    void setFolder(String folder) ;
}
