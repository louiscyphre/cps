package cps.server.session;

import java.sql.Connection;
import java.sql.SQLException;

import cps.common.Utilities;
import cps.entities.models.Customer;
import cps.entities.people.User;
import cps.server.ServerException;

public class CustomerSession extends BasicSession {
	protected Customer customer;

	public CustomerSession() {
		this.customer = null;
	}

	public CustomerSession(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public User getUser() {
		return getCustomer();
	}

	public Customer requireCustomer() throws ServerException {
		if (customer == null) {
			throw new ServerException("This action requires a logged in customer");
		}

		return customer;
	}

	public boolean findCustomer(Connection conn, int customerID) throws SQLException {
		customer = Customer.findByID(conn, customerID);
		return customer != null;
	}

	public Customer findCustomerEx(Connection conn, int customerID) throws SQLException, ServerException {
		if (!findCustomer(conn, customerID)) {
			throw new ServerException("Failed to find customer with id " + customerID);
		}

		return customer;
	}

	public boolean registerCustomer(Connection conn, String email) throws SQLException {
		String password = Utilities.randomString("0123456789", 4);
		// System.out.println(String.format("Sending password '%s' to email %s",
		// password, email));
		customer = Customer.create(conn, email, password);
		return customer != null;
	}

	public Customer registerCustomerEx(Connection conn, String email) throws SQLException, ServerException {
		if (!registerCustomer(conn, email)) {
			throw new ServerException("Failed to register customer with email " + email);
		}

		return customer;
	}

	public boolean findOrRegisterCustomer(Connection conn, int customerID, String email) throws SQLException {
		// If customerID != 0 -> we try to find an existing customer
		// If customerID == 0 -> we create a new customer

		if (customerID != 0) {
			return findCustomer(conn, customerID);
		}

		return registerCustomer(conn, email);
	}

	public Customer requireRegisteredCustomer(Connection conn, int customerID, String email)
			throws SQLException, ServerException {
		if (customerID != 0) {
			return findCustomerEx(conn, customerID);
		}

		return registerCustomerEx(conn, email);
	}
}
