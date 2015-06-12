package com.andrestrequest.http;

import com.andrestrequest.AndRestException;

public class ServerException extends AndRestException{

	private static final long serialVersionUID = 3153750355951678656L;

	public ServerException(ErrorMessage message) {
		super(message.getErrorMessage());
		// TODO Auto-generated constructor stub
	}
	
	public ServerException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ServerException(String msg, Throwable e) {
		super(msg, e);
		// TODO Auto-generated constructor stub
	}

}
