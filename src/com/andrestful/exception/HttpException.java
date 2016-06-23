package com.andrestful.exception;

/**
 * Exception condition when executing HTTP requests to API
 * @author s0pau
 */
public class HttpException extends RestfulException {

	private static final long serialVersionUID = -3285691804558117907L;

	private Integer statusCode;
	private String errorDetail;

	public HttpException(String msg, int statusCode, String errorDetail) {
		super(msg);
		this.statusCode = statusCode;
		this.errorDetail = errorDetail;
	}

	public HttpException(String msg, Exception e) {
		super(msg, e);
	}

	public String getLocalizedMessage() {
		if (statusCode == null) {
			return super.getLocalizedMessage();
		}

		StringBuilder sb = new StringBuilder(getMessage());
		sb.append("[Status code: ").append(statusCode).append(".");
		if (errorDetail != null && errorDetail.length() > 0) {
			sb.append("; Reason: ").append(errorDetail);
		}
		sb.append("]");
		return sb.toString();
	}

	public int getStatusCode() {
		return statusCode == null ? 0 : statusCode;
	}
}