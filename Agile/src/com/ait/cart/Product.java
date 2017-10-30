package com.ait.cart;

public class Product {
	private final String productCode;
	private final int price;
	private final String description;

	public Product(final String description, final int price,
			final String productCode) {
		this.description = description;
		this.price = price;
		this.productCode = productCode;
	}

	public int getPrice() {
		return price;
	}

	public String getProductCode() {
		return productCode;
	}

}
