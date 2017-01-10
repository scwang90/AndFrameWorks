package com.andmail.api;

/**
 * 邮件发送核心
 * Created by SCWANG on 2017/1/10.
 */

public interface MailKerneler {

    MailKerneler create(String from, String to) throws Exception;

    MailKerneler setContent(String subject, String content) throws Exception;

    MailKerneler send() throws Exception;
}
