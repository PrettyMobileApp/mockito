package com.ait.shopping;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ait.cart.CartItem;
import com.ait.cart.Customer;
import com.ait.cart.Cart;
import com.ait.cart.Product;
import com.ait.dao.CartDAO;
import com.ait.exception.CartDAOException;
import com.ait.exception.CartEmptyException;
import com.ait.exception.CartException;
import com.ait.exception.CartNotFoundException;
import com.ait.exception.CartUserException;
import com.ait.payment.CreditCardStrategy;
import com.ait.payment.PaymentStrategy;
import com.ait.payment.PaypalStrategy;
import com.ait.services.CreditCardHandler;
import com.ait.services.PayPalHandler;
import com.ait.services.PaymentHandlerFactory;
import com.ait.services.Invoicer;
import com.ait.services.OrderDispatcher;

public class CheckoutImpl {

	private final OrderDispatcher orderDispatcher;
	private final Invoicer invoicer;
	private final CartDAO cartDAO;
	private Customer customer;
	private long customerAccountId;
	private Cart cart;
	private final PaymentHandlerFactory paymentHandlerFactory;

	public CheckoutImpl(final OrderDispatcher orderDispatcher,
			final Invoicer invoicer, final CartDAO cartDAO,
			final PaymentHandlerFactory paymentHandlerFactory) {
		this.cartDAO = cartDAO;
		this.orderDispatcher = orderDispatcher;
		this.invoicer = invoicer;
		this.paymentHandlerFactory = paymentHandlerFactory;
	}

	public void checkOutCart(final long customerAccountId)
			throws CartException, SQLException {
		try {
			this.customerAccountId = customerAccountId;
			customer = cartDAO.findCustomerForId(customerAccountId);
			if (customer != null) {
				processCart(customer);
			} else {
				throw new CartUserException(customerAccountId);

			}
		} catch (SQLException e) {
			throw new CartDAOException(customerAccountId);
		}
	}

	private void processCart(final Customer customer) throws CartException {
		cart = customer.getCart();
		if (cart == null) {
			throw new CartNotFoundException(customerAccountId);
		} else {
			final ArrayList<CartItem> cartItems = (ArrayList<CartItem>) cart
					.getCartItems();
			if (cartItems.isEmpty()) {
				throw new CartEmptyException(customerAccountId);
			}
			final int cartTotal = cart.getTotalPrice();
			final String invoiceNumber = sendInvoice(cartTotal);
			processPayment(invoiceNumber, cartTotal);
			dispatchItems(cartItems);

		}

	}

	private String sendInvoice(final int cartTotal) {
		return invoicer.invoiceCustomer(customer.getAccountNumber(),
				customer.getEmail(), cartTotal);
	}

	private void dispatchItems(final ArrayList<CartItem> cartItems) {
		Product product;
		for (final CartItem cartItem : cartItems) {
			product = cartItem.getProduct();
			orderDispatcher.dispatchItem(product.getProductCode(),
					cartItem.getQty(), customer.getName(),
					customer.getAddress());
		}
	}

	private void processPayment(final String invoiceNumber, final int cartTotal) {
		final PaymentStrategy paymentStrategy = customer.getPaymentStrategy();
		if (customer.getPaymentType().equals("CreditCard")) {
			final CreditCardStrategy creditCardStrategy = ((CreditCardStrategy) paymentStrategy);
			final CreditCardHandler creditCardHandler = (CreditCardHandler) paymentHandlerFactory
					.getPaymentHandler("CreditCard");
			creditCardHandler.pay(invoiceNumber,
					creditCardStrategy.getCardNumber(),
					creditCardStrategy.getCardName(),
					creditCardStrategy.getCvv(),
					creditCardStrategy.getDateOfExpiry(), cartTotal);
		} else {
			final PaypalStrategy payPalStrategy = ((PaypalStrategy) paymentStrategy);
			final PayPalHandler payPalHandler = (PayPalHandler) paymentHandlerFactory
					.getPaymentHandler("PayPal");
			payPalHandler.pay(invoiceNumber, payPalStrategy.getEmailId(),
					payPalStrategy.getPassword(), cartTotal);
		}
	}

}
