package com.ait.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.Times;

import static org.junit.Assert.*;

import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import com.ait.cart.Cart;
import com.ait.cart.CartItem;
import com.ait.cart.Customer;
import com.ait.cart.Product;
import com.ait.dao.CartDAO;
import com.ait.payment.CreditCardStrategy;
import com.ait.payment.PaypalStrategy;
import com.ait.services.CreditCardHandler;
import com.ait.services.PayPalHandler;
import com.ait.services.PaymentHandler;
import com.ait.services.PaymentHandlerFactory;
import com.ait.services.Invoicer;
import com.ait.services.OrderDispatcher;
import com.ait.shopping.Checkout;
import com.ait.shopping.CheckoutImpl;
import com.ait.exception.CartDAOException;
import com.ait.exception.CartException;
import com.ait.exception.CartNotFoundException;
import com.ait.exception.CartUserException;
import com.ait.exception.CartEmptyException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;

public class MockitoShopCartTest {

	private CartDAO dao;
	private CreditCardHandler creditCardHandler;
	private Invoicer invoicer;
	private OrderDispatcher orderDispatcher;
	private PaymentHandler paymentHandler;
	private PaymentHandlerFactory paymentHandlerFactory;
	private PayPalHandler paypalHandler;
	private Checkout checkout;
	private CheckoutImpl checkoutImpl;
	private Long customerId;
	private Customer customer;
	private Cart cart;
	private CartItem cartItem;
	private Product product;
	
	
	@Before
	public void setUp() throws Exception {
		dao = mock(CartDAO.class);
		creditCardHandler = mock(CreditCardHandler.class);
		invoicer = mock(Invoicer.class);
		orderDispatcher = mock(OrderDispatcher.class);
		paymentHandler = mock(PaymentHandler.class);
		paymentHandlerFactory = mock(PaymentHandlerFactory.class);
		paypalHandler = mock(PayPalHandler.class);
		checkout = mock(Checkout.class);
		
		product = new Product("bread",1,"123");
		cartItem = new CartItem(product, 1);
		cart = new Cart();
		customer = new Customer();
		customerId = (long) 10;
		checkoutImpl = new CheckoutImpl(orderDispatcher, invoicer, dao, paymentHandlerFactory);
		
	}

	@Test(expected = CartUserException.class)
	public void testUserNotFoundException() throws CartException, SQLException {
		when(dao.findCustomerForId(customerId)).thenReturn(null);		
		checkoutImpl.checkOutCart(customerId);
		
	}//Test 1  testUserNotFoundException
	
	@Test(expected = CartNotFoundException.class)
	public void testCartNotFoundException() throws SQLException, CartException  {
		when(dao.findCustomerForId(customerId)).thenReturn(customer);
		
		checkoutImpl.checkOutCart(customerId);
		verify(dao, new Times(1)).findCustomerForId(customerId);
	}//Test 2  testCartNotFoundException
	
	@Test(expected = CartEmptyException.class)
	public void testCartEmptyException() throws SQLException, CartException  {
		when(dao.findCustomerForId(customerId)).thenReturn(customer);		
		customer.setCart(cart);
		
		checkoutImpl.checkOutCart(customerId);
		verify(dao, new Times(1)).findCustomerForId(customerId);
	}//Test 3  testCartEmptyException
		
	@Test(expected = CartDAOException.class)
	public void testCartDAOException() throws SQLException, CartException  {
		when(dao.findCustomerForId(customerId)).thenThrow(SQLException.class);	
		
		checkoutImpl.checkOutCart(customerId);
		verify(dao, new Times(1)).findCustomerForId(customerId);
	}//Test 4  testCartDAOException
	
	@Test
	public void testOneItemInCartPayPal() throws SQLException, CartException  {
		when(dao.findCustomerForId(customerId)).thenReturn(customer);	
		customer.setCart(cart);
		customer.setPaymentType("PayPal");
		customer.setName("joe");
		customer.setAddress("onTheRoad");
		PaypalStrategy paypalStrategy = new PaypalStrategy("joe@gmail.com","password123");
		customer.setPaymentStrategy(paypalStrategy);
		cart.addItem(product, 1);
		when(paymentHandlerFactory.getPaymentHandler(customer.getPaymentType())).thenReturn(paypalHandler);
		
		checkoutImpl.checkOutCart(customerId);
		
		
		verify(invoicer, new Times(1)).invoiceCustomer(customerId, "joe@gmail.com", 1);
		verify(paypalHandler,new Times(1)).pay(anyString(), eq("joe@gmail.com"),eq("password123"),eq(1));
		verify(orderDispatcher, new Times(1)).dispatchItem("123", 1, "joe", "onTheRoad");
		
	}//Test5  testOneItemInCartPayPal
		
	//Test 6  testOneItemInCartCreditCard()
		
	//Test 7  testTwoItemsInCartPayPal
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

