package com.andrestrequest.http;


public class ServerCodeException extends ServerException{

	private static final long serialVersionUID = 3153750355951678656L;
	
	private String errorcode = "-1";

	public ServerCodeException(ErrorMessage message) {
		super(message);
		errorcode = message.getCode();
	}

	public ServerCodeException(String msg, Throwable e) {
		super(msg, e);
	}

	public String getErrorcode() {
		return errorcode;
	}
}
