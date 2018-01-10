package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import cps.api.request.LoginRequest;
import cps.api.response.LoginResponse;
import cps.api.response.ServerResponse;
import cps.entities.models.Customer;
import cps.server.ServerController;

public class CustomerController extends RequestController {

	public CustomerController(ServerController serverController) {
		super(serverController);
	}
	
	public ServerResponse handle(LoginRequest request) {
		return databaseController.performQuery(conn -> {
			LoginResponse response = new LoginResponse(false, "", 0);


			Customer customer = Customer.findByEmailAndPassword(conn, request.getEmail(), request.getPassword());

			if (customer == null) {
				response.setError("Login failed, invalid email or password.");
				return response;
			}
			
			response.setCustomerID(customer.getId());
			response.setSuccess("Login successful");
			return response;
		});
	}
	
	public static boolean chargeCustomer(Connection conn, ServerResponse response, Customer customer, float sum) throws SQLException {
		customer.addDebit(sum);

		if (!customer.update(conn)) {
			response.setError("Failed to update customer");
			return false;
		}
		
		return true;
	}

}
