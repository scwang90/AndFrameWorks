// Copyright (c) 2003-2013, LogMeIn, Inc. All rights reserved.
// This is part of Xively4J library, it is under the BSD 3-Clause license.
package com.andrestrequest.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.andrestrequest.AndRestConfig;
import com.andrestrequest.util.GsonUtil;
import com.andrestrequest.util.exception.ParseToObjectException;

/**
 * Class for handling the http response
 * @author s0pau
 * @param <T>
 * 
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

		try {
			retval.setBody(parseHttpEntity(response.getEntity()));
		} catch (IOException e) {
			throw new HttpException(
					"Http response [%s] but body cannot be parsed.", e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new ParseToObjectException("Json解析异常", e);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			throw new ParseToObjectException("Json解析异常", e);
		}

		Map<String, String> headers = new HashMap<String, String>();
		for (Header header : response.getAllHeaders()) {
			headers.put(header.getName(), header.getValue());
		}
		retval.setHeaders(headers);

		return retval;
	}
	
	private String parseHttpEntity(HttpEntity entity) throws IOException, ParseException, JSONException {
		String response = EntityUtils.toString(entity, AndRestConfig.getCharset());
		if (DEBUG)
		{
			System.out.println("Handling response "+response);
		}
		if (!JSONFRAMEWORK || STATUS==null || STATUS_OK==null || RESULT == null) {
			return response;
		}
		try {
			JSONObject object = new JSONObject(response);
			if (STATUS_OK.equals(object.get(STATUS))) {
				return object.get(RESULT).toString();
				// return root.getJSONArray(RESULT);
			} else {
				String errormessage = object.get(RESULT).toString();
				try {
					ErrorMessage message = GsonUtil.toObject(errormessage, ErrorMessageClass);
					throw new ServerCodeException(message);
				} catch (ParseException e) {
					// TODO: handle exception
					System.out.println("errormessage = "+errormessage);
					throw new ServerException(response);
				} catch (ParseToObjectException e) {
					// TODO: handle exception
					System.out.println("errormessage = "+errormessage);
					throw new ServerException(response);
				}
			}
		} catch (ParseException e) {
			// TODO: handle exception
			System.out.println("response = "+response);
			throw new ServerException(response);
		} catch (JSONException e) {
			// TODO: handle exception
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
