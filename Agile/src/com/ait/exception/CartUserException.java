package com.ait.exception;

public class CartUserException extends CartException {

	private static final long serialVersionUID = 334051992916748022L;

	public CartUserException(final long customerAccountId) {
		super("Unknown Customer: "+ customerAccountId);
	}

}
