package com.andmail.api;

import com.andmail.api.model.MailSenderModel;

/**
 * 右键发送器
 * Created by SCWANG on 2017/1/10.
 */

public interface MailSender {

    void send() throws Exception;

    void sendTask();

    void onTaskException(Exception e);

    MailKerneler newMailKerneler(MailSenderModel model);
}
