// Copyright (c) 2003-2013, LogMeIn, Inc. All rights reserved.
// This is part of Xively4J library, it is under the BSD 3-Clause license.
package com.andrestrequest.http;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.CoreConnectionPNames;

import com.andrestrequest.AndRestConfig;

/**
 * Class for creating HttpClient and allowing downstream application to
 * configure HTTP client behaviours such as retry and timeouts, abstracted from
 * underlying implementation.
 * 
 * @author s0pau
 */
@SuppressWarnings("deprecation")
public class HttpClientBuilder
{
	private static final int DEFAULT_CONNECTION_TIMEOUT_IN_MS = 3000;
	private static final int DEFAULT_SOCKET_TIMEOUT_IN_MS = 3000;

	private HttpRequestRetryHandler retryHandler;
	private DefaultHttpClient httpClient;
	private int socketTimeout;
	private int connectionTimeout;

	private static HttpClientBuilder instance;

	private HttpClientBuilder()
	{
		Integer userConnectionTimeout = AndRestConfig.getConnectionTimeout();
		connectionTimeout = userConnectionTimeout == null ? DEFAULT_CONNECTION_TIMEOUT_IN_MS : userConnectionTimeout;
		Integer userSocketTimeout = AndRestConfig.getSocketTimeout();
		socketTimeout = userSocketTimeout == null ? DEFAULT_SOCKET_TIMEOUT_IN_MS : userSocketTimeout;
	}

	public static HttpClientBuilder getInstance()
	{
		if (instance == null)
		{
			instance = new HttpClientBuilder();
		}
		return instance;
	}

	/**
	 * @param retryCount
	 *            number of retries to be attempted by the http client
	 */
	public void setRetryCount(int retryCount)
	{
		retryHandler = new DefaultHttpRequestRetryHandler(retryCount, false);
	}

	/**
	 * @param connectionTimeout
	 *            number of milliseconds before timeout when establising
	 *            connection, default is DEFAULT_CONNECTION_TIMEOUT_IN_MS
	 */
	public void setConnectionTimeout(int connectionTimeout)
	{
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * @param socketTimeout
	 *            number of milliseconds before timeout when waiting for packet
	 *            response, default is DEFAULT_SOCKET_TIMEOUT_IN_MS
	 */
	public void setSocketTimeout(int socketTimeout)
	{
		this.socketTimeout = socketTimeout;
	}

	/**
	 * @return an HttpClient with config as specified in this builder
	 */
	DefaultHttpClient getHttpClient()
	{
		if (httpClient == null)
		{
			httpClient = new DefaultHttpClient();
			if (retryHandler == null)
			{
				retryHandler = new DefaultHttpRequestRetryHandler(0, false);
			}
			httpClient.setHttpRequestRetryHandler(retryHandler);
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeout);
		}
		return httpClient;
	}
}
