package com.ait.shopping;

import com.ait.exception.CartException;

public interface Checkout {

	/**
	 * Download a movie from this store.
	 * @param accountId the account ID of the user who wishes to download the movie.
	 * @param movieTitle the title of the movie.
	 * @return the movie which has been downloaded.
	 * @throws CartException if the movie could not be downloaded.
	 */
	
	 void process(String accountId) throws CartException;
}
