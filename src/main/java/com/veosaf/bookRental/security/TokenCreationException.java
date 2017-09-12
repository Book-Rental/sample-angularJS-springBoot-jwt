package com.veosaf.bookRental.security;

import org.springframework.security.core.AuthenticationException;

public class TokenCreationException extends AuthenticationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6133901873791900924L;
	

	public TokenCreationException(String msg) {
		super(msg);
	}
	
	public TokenCreationException(String msg, Throwable t) {
		super(msg, t);
	}

}
