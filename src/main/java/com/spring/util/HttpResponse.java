package com.spring.util;

import org.springframework.http.HttpStatus;

public class HttpResponse<T> {

	private int httpStatusCode;

	private HttpStatus httpStatus;

	private String reason;

	private String message;

	private T object;

	public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message, T object) {
		super();
		this.setHttpStatusCode(httpStatusCode);
		this.httpStatus = httpStatus;
		this.reason = reason;
		this.message = message;
		this.object = object;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

}
