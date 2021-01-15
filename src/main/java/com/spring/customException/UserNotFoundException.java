package com.spring.customException;

public class UserNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4514903920562791329L;

	public UserNotFoundException(String message) {
		super(message);
	}
}
