package com.ait.services;

public interface Invoicer {

	String invoiceCustomer(long customerNumber, String customerEmail,
			float amount);

}
