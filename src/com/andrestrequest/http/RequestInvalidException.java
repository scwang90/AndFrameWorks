// Copyright (c) 2003-2013, LogMeIn, Inc. All rights reserved.
// This is part of Xively4J library, it is under the BSD 3-Clause license.
package com.andrestrequest.http;

import com.andrestrequest.AndRestException;

/**
 * Exception condition when constructing requests
 * 
 * @author s0pau
 * 
 */
public class RequestInvalidException extends AndRestException
{
	private static final long serialVersionUID = -3165313086442745433L;

	public RequestInvalidException(String msg, Throwable e)
	{
		super(msg, e);
	}
	
	public RequestInvalidException(String msg)
	{
		super(msg);
	}
}
