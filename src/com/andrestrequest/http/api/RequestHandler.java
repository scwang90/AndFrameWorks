// Copyright (c) 2003-2013, LogMeIn, Inc. All rights reserved.
// This is part of Xively4J library, it is under the BSD 3-Clause license.
package com.andrestrequest.http.api;

import com.andrestrequest.http.DefaultRequestHandler;
import com.andrestrequest.http.Response;

import org.apache.http.impl.client.DefaultHttpClient;

import java.util.Map;

@SuppressWarnings("deprecation")
public abstract class RequestHandler {

	private static RequestHandler instance;
	private DefaultHttpClient httpClient;

	public static RequestHandler getInstance() {
		if (instance == null) {
			instance = new DefaultRequestHandler();
		}
		return instance;
	}

	public Response doRequest(HttpMethod method, String path) throws Exception {
		return doRequest(method, path, null,null,null);
	}

	public Response doRequest(HttpMethod method, String path, Object body) throws Exception {
		return doRequest(method, path, null, body,null);
	}

	public Response doRequest(HttpMethod method, String path, Map<String, String> headers) throws Exception {
		return doRequest(method, path, headers, null,null);
	}

	public Response doRequest(HttpMethod method, String path, Map<String, String> headers, Object body) throws Exception {
		return doRequest(method, path, headers, body,null);
	}

	public Response doRequest(HttpMethod method, String path, Map<String, String> headers, Map<String, Object> params) throws Exception {
		return doRequest(method, path,headers,null, params);
	}

	public abstract Response doRequest(HttpMethod method, String path, Map<String, String> headers,
			Object body,Map<String, Object> params) throws Exception ;

}
