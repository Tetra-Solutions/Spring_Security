package com.spring.customException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.spring.util.HttpResponse;

@RestControllerAdvice
public class ExceptionHandling {

	private static final String ACCOUNT_DISABLED = "Your account has been disabled.";

	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<HttpResponse> accountDisabledException() {

		return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED);
	}

	@ExceptionHandler(EmailExistsException.class)
	public ResponseEntity<HttpResponse> emailExistsException(EmailExistsException e) {

		return createHttpResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {

		HttpResponse httpResponse = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase(),
				message.toUpperCase());

		return new ResponseEntity<HttpResponse>(httpResponse, httpStatus);
	}
}
