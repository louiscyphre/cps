package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import cps.api.response.ServerResponse;
import cps.common.Utilities.Pair;
import cps.entities.models.Customer;
import cps.server.ServerController;

public class UserController extends RequestController {

	public UserController(ServerController serverController) {
		super(serverController);
	}
	
	public Pair<Customer, ServerResponse> findOrCreateCustomer(Connection conn, int customerID) throws SQLException {
		Customer customer = null;
		ServerResponse response = null;
		
		if (customerID != 0) {
			customer = Customer.findByID(conn, customerID);
			
			if (customer == null) {
				response = ServerResponse.error("Failed to find customer with id " + customerID);
			}
		}
		
		return new Pair<Customer, ServerResponse>(customer, response);
	}

}
