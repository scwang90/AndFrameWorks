package com.andmail.exception;


/**
 *
 * Created by SCWANG on 2017/1/10.
 */

public class MailException extends Exception {

    public MailException() {
    }

    public MailException(String message) {
        super(message);
    }

    public MailException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailException(Throwable cause) {
        super(cause);
    }

}
