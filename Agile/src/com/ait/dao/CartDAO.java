package com.ait.dao;

import java.sql.SQLException;

import com.ait.cart.Customer;

public interface CartDAO  {
	Customer findCustomerForId(long customerAccountId) throws SQLException;
	
}
