package com.ait.cart;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	private final List<CartItem> cartItems = new ArrayList<CartItem>();


	public void addItem(final Product product, final int quantity) {
		final CartItem cartItem = new CartItem(product, quantity);
		cartItems.add(cartItem);
	}


	public int getTotalPrice() {
		int total = 0;
		for (final CartItem item : cartItems) {
			total += item.getProduct().getPrice() * item.getQty();
		}
		return total;
	}
	
	public List<CartItem> getCartItems(){
		return cartItems;
	}

}
