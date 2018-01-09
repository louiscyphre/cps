package cps.server.controllers;

import cps.api.request.LoginRequest;
import cps.api.response.LoginResponse;
import cps.api.response.ServerResponse;
import cps.entities.models.Customer;
import cps.server.ServerController;

public class UserController extends RequestController {

	public UserController(ServerController serverController) {
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

}
