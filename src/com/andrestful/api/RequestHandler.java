// Copyright (c) 2003-2013, LogMeIn, Inc. All rights reserved.
// This is part of Xively4J library, it is under the BSD 3-Clause license.
package com.andrestful.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings({"deprecation", "unused"})
public abstract class RequestHandler {

	private static RequestHandler instance;
	private static final String KEY_COOKIE = "RequestHandler.Cookie";

	protected Map<String, String> cookies = null;
	protected SharedPreferences mCookiePreferences;

	public RequestHandler enableCookie(Context context) {
		this.mCookiePreferences = context.getSharedPreferences(KEY_COOKIE, Context.MODE_PRIVATE);
		this.cookies = restoreCookie();
		return this;
	}

	public RequestHandler enableCookie(boolean enable) {
		if (enable && cookies == null) {
			this.cookies = restoreCookie();
		} else if (!enable && cookies != null) {
			this.cookies = null;
		}
		return this;
	}

	public RequestHandler enableCookie(Map<String, String> cookies) {
		this.cookies = cookies;
		return this;
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

	public Response doUpload(String path, Object... files) throws Exception {
		return doUpload(path, null, null, files);
	}

	public Response doUpload(String path, Map<String, Object> params, Object... files) throws Exception {
		return doUpload(path, null, params, files);
	}

	public Response doUpload(String path, Map<String, Object> params, String name, InputStream input) throws Exception {
		return doUpload(path, null, params, name, input);
	}

	public abstract Response doUpload(String path, Map<String, String> headers, Map<String, Object> params, Object... files) throws Exception;

	public abstract Response doUpload(String path, Map<String, String> headers, Map<String, Object> params, String name, InputStream input) throws Exception;

	public abstract Response doRequest(HttpMethod method, String path, Map<String, String> headers, Object body, Map<String, Object> params) throws Exception ;

	protected String getCookie() {
		if (cookies != null && !cookies.isEmpty()) {
			StringBuilder cookie = new StringBuilder();
			for (Map.Entry<String, String> entry : cookies.entrySet()) {
				cookie.append(";");
				cookie.append(entry.getValue());
			}
			return cookie.substring(1);
		} else {
			return "";
		}
	}

	protected boolean updateCookie(String set_cookie) {
		if (cookies != null && set_cookie != null && set_cookie.length() > 0) {
			String[] setcookies = set_cookie.split(";");
			for (String setcookie : setcookies) {
				int index = setcookie.indexOf('=');
				if (index > 0) {
					String key = setcookie.substring(0, index);
					cookies.put(key, setcookie);
				}
			}
			saveCookie(cookies);
			return true;
		}
		return false;
	}

	protected void saveCookie(Map<String, String> cookies) {
		if (mCookiePreferences != null) {
			mCookiePreferences.edit().putString(KEY_COOKIE, getCookie()).apply();
		}
	}

	protected Map<String, String> restoreCookie() {
		if (mCookiePreferences != null) {
			cookies = new LinkedHashMap<>();
			updateCookie(mCookiePreferences.getString(KEY_COOKIE, ""));
		}
		return new LinkedHashMap<>();
	}

	public void clearCookies() {
		if (cookies != null) {
			cookies.clear();
		}
		if (mCookiePreferences != null) {
			mCookiePreferences.edit().putString(KEY_COOKIE, "").apply();
		}
	}
}
