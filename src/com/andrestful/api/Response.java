// Copyright (c) 2003-2013, LogMeIn, Inc. All rights reserved.
// This is part of Xively4J library, it is under the BSD 3-Clause license.
package com.andrestful.api;

import com.andrestful.exception.ServerException;
import com.andrestful.util.GsonUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 返回值
 * @author 树朾
 */
public abstract class Response {
	
	protected int statusCode;
	protected Map<String, String> headers;
	protected String orgbody;
	protected String body;

	public Response() {
	}

	public Response(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getHeaders(String key) {
		return headers.get(key);
	}

	/**
	 * Shallow cope the collection of headers
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = new HashMap<>();
		this.headers.putAll(headers);
	}

	public String getBody() {
		parser();
		if (body == null) {
			return orgbody;
		}
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOrgbody() {
		return orgbody;
	}

	public void setOrgbody(String orgbody) {
		this.orgbody = orgbody;
	}

	public <T> T to(Class<T> returnType) throws IOException {
		parser();
		if (body == null) {
			return GsonUtil.toObject(orgbody, returnType);
		}
		return GsonUtil.toObject(body, returnType);
	}

	public <T> List<T> toList(Class<T> returnType)
			throws Exception {
		parser();
		if (body == null) {
			return GsonUtil.toObjects(orgbody, returnType);
		}
		return GsonUtil.toObjects(body, returnType);
	}

	public <T> T orgTo(Class<T> returnType) throws IOException {
		return GsonUtil.toObject(orgbody, returnType);
	}

	public <T> List<T> orgToList(Class<T> returnType) throws Exception {
		return GsonUtil.toObjects(orgbody, returnType);
	}

	public JSONObject getJSONObject() throws Exception {
		try {
			parser();
			return new JSONObject(body);
		} catch (Throwable e) {
			throw new ServerException("服务器返回格式异常："+body,e);
		}
	}

	public JSONObject getOrgJSONObject() {
		try {
			return new JSONObject(orgbody);
		} catch (Throwable e) {
			throw new ServerException("服务器返回格式异常："+orgbody,e);
		}
	}

	public abstract Response parser();
}
