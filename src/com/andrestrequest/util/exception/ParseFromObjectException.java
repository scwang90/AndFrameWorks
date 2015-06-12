// Copyright (c) 2003-2013, LogMeIn, Inc. All rights reserved.
// This is part of Xively4J library, it is under the BSD 3-Clause license.
package com.andrestrequest.util.exception;

import com.andrestrequest.AndRestException;

/**
 * Exception condition when parsing from {@link DomainObject}
 * 
 * @author s0pau
 * 
 */
public class ParseFromObjectException extends AndRestException
{
	private static final long serialVersionUID = 1417537723548831992L;

	public ParseFromObjectException(String msg, Throwable e) {
		super(msg, e);
		// TODO Auto-generated constructor stub
	}

	public ParseFromObjectException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	
}
