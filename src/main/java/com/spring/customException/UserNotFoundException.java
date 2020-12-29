package com.spring.customException;

public class UserNotFoundException extends Exception {

	public UserNotFoundException(String message) {
		super(message);
	}
}
