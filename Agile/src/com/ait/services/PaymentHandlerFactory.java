package com.ait.services;

public interface PaymentHandlerFactory {
	 PaymentHandler getPaymentHandler(String type);
}
