package com.ait.exception;

public class CartDAOException extends CartException {

	private static final long serialVersionUID = 334051992916748022L;

	public CartDAOException(final long customerAccountId) {
		super("Error connection to database: "+ customerAccountId);
	}

}
