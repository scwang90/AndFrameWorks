package com.andrestful.exception;


import com.andrestful.api.ErrorMessage;

public class RestfulException extends RuntimeException{

	private static final long serialVersionUID = 3153750355951678656L;

	public RestfulException(ErrorMessage message) {
		super(message.getErrorMessage());
	}

	public RestfulException(String message) {
		super(message);
	}

	public RestfulException(String msg, Throwable e) {
		super(msg, e);
	}

}
