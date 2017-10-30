package com.ait.services;


public interface CreditCardHandler extends PaymentHandler{
	void pay(String invoiceId,String cardNumber, String cardName, String ccv,
			String expiryDate, int amount);
}
