// Copyright (c) 2003-2013, LogMeIn, Inc. All rights reserved.
// This is part of Xively4J library, it is under the BSD 3-Clause license.
package com.andrestrequest.http;

import com.andrestrequest.AndRestConfig;
import com.andrestrequest.util.GsonUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for handling the http response
 * @author s0pau
 */
@SuppressWarnings("deprecation")
public class DefaultResponseHandler implements ResponseHandler<Response> {

	public static String STATUS = null;
	public static Object STATUS_OK = null;
	public static String RESULT = null;
	public static int SUCCESSCODE = 200;
	public static boolean JSONFRAMEWORK = true;
	
	public static Class<? extends ErrorMessage> ErrorMessageClass = null;

	public static boolean DEBUG = false;

	public boolean jsonframework = true;
	public String status = null;
	public Object status_ok = null;
	public String result = null;

	public DefaultResponseHandler(){
		this.status = STATUS;
		this.status_ok = STATUS_OK;
		this.result = RESULT;
		this.jsonframework = JSONFRAMEWORK;
	}

	public DefaultResponseHandler(boolean jsonframework){
		this.status = STATUS;
		this.status_ok = STATUS_OK;
		this.result = RESULT;
		this.jsonframework = jsonframework;
	}

	public DefaultResponseHandler(String STATUS,Object STATUS_OK,String RESULT){
		this.status = STATUS;
		this.status_ok = STATUS_OK;
		this.result = RESULT;
		this.jsonframework = true;
	}

	public Response handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		int statusCode = response.getStatusLine().getStatusCode();

		if (!isHttpStatusOK(statusCode)) {
			String errorDetail = null;
			try {
				errorDetail = EntityUtils.toString(response.getEntity(), AndRestConfig.getCharset());
			} catch (IOException swallow) {
			}
			throw new HttpException(
					"Http response "+statusCode,
					statusCode, errorDetail);
		}

		Response retval = new Response(statusCode);

		retval.setBody(parseHttpEntity(response.getEntity()));

		Map<String, String> headers = new HashMap<String, String>();
		for (Header header : response.getAllHeaders()) {
			headers.put(header.getName(), header.getValue());
		}
		retval.setHeaders(headers);

		return retval;
	}
	
	private String parseHttpEntity(HttpEntity entity) throws IOException{
		String response = EntityUtils.toString(entity, AndRestConfig.getCharset());
		if (DEBUG)
		{
			System.out.println("Handling response "+response);
		}
		if (!jsonframework || status==null || status_ok==null || result == null) {
			return response;
		}
		try {
			JSONObject object = new JSONObject(response);
			if (status_ok.equals(object.get(status))) {
				return object.get(result).toString();
				// return root.getJSONArray(RESULT);
			} else {
				String errormessage = object.get(result).toString();
				try {
					ErrorMessage message = GsonUtil.toObject(errormessage, ErrorMessageClass);
					throw new ServerCodeException(message);
				} catch (ServerCodeException e) {
					throw e;
				} catch (Throwable e) {
					throw new ServerException(errormessage);
				}
			}
		} catch (Throwable e) {
			System.out.println("response = "+response);
			throw new ServerException(response);
		}
	}

	private boolean isHttpStatusOK(int statusCode) {
		if (statusCode != SUCCESSCODE) {
			return false;
		}
		return true;
	}

}
