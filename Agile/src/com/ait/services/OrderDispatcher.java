package com.ait.services;

public interface OrderDispatcher {

	void dispatchItem(String productCode, int quantity, String customerName,
			String customerAddress);

}
