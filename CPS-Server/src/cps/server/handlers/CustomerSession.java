package cps.server.handlers;

import java.sql.Connection;
import java.sql.SQLException;

import cps.api.response.ServerResponse;
import cps.entities.models.Customer;

public class CustomerSession {
	protected Customer customer = null;

	public Customer getCustomer() {
		return customer;
	}

	public boolean findCustomer(Connection conn, ServerResponse response, int customerID) throws SQLException {

		customer = Customer.findByID(conn, customerID);

		if (customer == null) {
			response.setError("Failed to find customer with id " + customerID);
		}

		return customer != null;
	}

	public boolean registerCustomer(Connection conn, ServerResponse response, int customerID, String email) throws SQLException {
		String password = "1234"; // TODO: generate password
		System.out.println(String.format("Sending password '%s' to email %s", password, email));
		customer = Customer.create(conn, email, password);
		return customer != null;
	}

	public boolean findOrRegisterCustomer(Connection conn, ServerResponse response, int customerID, String email) throws SQLException {
		// If customerID != 0 -> we try to find an existing customer
		// If customerID == 0 -> we create a new customer

		if (customerID != 0) {
			return findCustomer(conn, response, customerID);
		}

		return registerCustomer(conn, response, customerID, email);
	}
}
