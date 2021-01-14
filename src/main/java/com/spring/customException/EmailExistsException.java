package com.spring.customException;

public class EmailExistsException extends Exception {

	public EmailExistsException(String message) {
		super(message);
	}
}
