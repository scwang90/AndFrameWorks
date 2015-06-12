package com.andrestrequest.http;


public class ServerCodeException extends ServerException{

	private static final long serialVersionUID = 3153750355951678656L;
	
	private String errorcode = "-1";

	public ServerCodeException(ErrorMessage message) {
		super(message);
		// TODO Auto-generated constructor stub
		errorcode = message.getCode();
	}

	public ServerCodeException(String msg, Throwable e) {
		super(msg, e);
		// TODO Auto-generated constructor stub
	}

	public String getErrorcode() {
		return errorcode;
	}
}
