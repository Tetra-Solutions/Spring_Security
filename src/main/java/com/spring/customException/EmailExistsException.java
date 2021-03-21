package com.spring.customException;

public class EmailExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 506137908594080329L;

	public EmailExistsException(String message) {
		super(message); 
	}
}
