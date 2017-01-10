package com.andmail.model;

import com.andmail.api.model.MailSenderModel;

/**
 * 发送数据model
 * Created by SCWANG on 2017/1/10.
 */

public class AmSenderModel extends AmMailModel implements MailSenderModel {

    public String sendto;
    public String from;

    @Override
    public String getSendto() {
        return sendto;
    }

    @Override
    public void setSendto(String sendto) {
        this.sendto = sendto;
    }

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public void setFrom(String from) {
        this.from = from;
    }
}
