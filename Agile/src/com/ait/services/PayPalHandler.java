package com.ait.services;

public interface PayPalHandler extends PaymentHandler {
	void pay(String invoiceId, String email, String password, int amount);
}
