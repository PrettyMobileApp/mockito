package com.ait.cart;

public class CartItem {
	private final Product product;
	private final int quantity;
	
	public CartItem(final Product product, final int quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public int getQty() {
		return quantity;
	}
	
}
