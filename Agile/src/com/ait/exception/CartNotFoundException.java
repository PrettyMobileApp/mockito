package com.ait.exception;

/**
 * General exception for a cart not found for a customer.
 */
public class CartNotFoundException extends CartException {

	private static final long serialVersionUID = -7844164306968873458L;

	public CartNotFoundException(final long customerAccountId) {
		super("No shoppping cart found: "+ customerAccountId);
	}

}
