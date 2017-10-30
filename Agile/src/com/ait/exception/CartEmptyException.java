package com.ait.exception;

public class CartEmptyException extends CartException {

	private static final long serialVersionUID = 334051992916748022L;

	public CartEmptyException(final long customerAccountId) {
		super("Cart is Empty: "+ customerAccountId);
	}

}
