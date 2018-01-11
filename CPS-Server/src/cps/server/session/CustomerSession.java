package cps.server.session;

import java.sql.Connection;
import java.sql.SQLException;

import cps.api.response.ServerResponse;
import cps.common.Utilities;
import cps.entities.models.Customer;
import cps.entities.people.User;

public class CustomerSession implements UserSession {
	protected Customer customer = null;
	protected UserSession newSession = null;

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
		String password = Utilities.randomString("0123456789", 4);
//		System.out.println(String.format("Sending password '%s' to email %s", password, email));
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

	@Override
	public User getUser() {
		return getCustomer();
	}

	@Override
	public void setNewSession(UserSession session) {
		newSession = session;
	}

	@Override
	public UserSession getNewSession() {
		return newSession;
	}
}
