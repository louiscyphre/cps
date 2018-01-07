package cps.server.controllers;

import cps.api.response.*;
import cps.entities.models.Customer;
import cps.entities.models.SubscriptionService;

import java.time.LocalDate;
import java.time.LocalTime;

import cps.api.request.FullSubscriptionRequest;
import cps.api.request.RegularSubscriptionRequest;
import cps.api.request.SubscriptionRequest;
import cps.server.ServerController;
import cps.server.handlers.CustomerSession;

// TODO: handle payment for subscription
public class SubscriptionController extends RequestController {

	public SubscriptionController(ServerController serverController) {
		super(serverController);
	}
	
	public ServerResponse handle(FullSubscriptionRequest request) {
		LocalDate startDate = request.getStartDate();
		LocalDate endDate = startDate.plusDays(28);
		LocalTime dailyExitTime = LocalTime.of(0, 0, 0);
		FullSubscriptionResponse response = new FullSubscriptionResponse(false, "");
		return handle(request, response, startDate, endDate, dailyExitTime);
	}
	
	public ServerResponse handle(RegularSubscriptionRequest request) {
		LocalDate startDate = request.getStartDate();
		LocalDate endDate = startDate.plusDays(28);
		LocalTime dailyExitTime = request.getDailyExitTime();
		RegularSubscriptionResponse response = new RegularSubscriptionResponse(false, "");
		return handle(request, response, startDate, endDate, dailyExitTime);
	}
	
	public ServerResponse handle(SubscriptionRequest request, SubscriptionResponse response, LocalDate startDate, LocalDate endDate, LocalTime dailyExitTime) {
		return databaseController.performQuery(conn -> {			
			// TODO: check request parameters
			CustomerSession session = new CustomerSession();
			session.findOrRegisterCustomer(conn, response, request.getCustomerID(), request.getEmail());
			
			Customer customer = session.getCustomer(); // By this time customer has to exist
			
			if (customer == null) {
			 	return response; // the response was modified by the customer session
			}

			SubscriptionService result = SubscriptionService.create(conn, request.getSubscriptionType(), customer.getId(),
					request.getEmail(), request.getCarID(), request.getLotID(), startDate, endDate, dailyExitTime);

			if (result == null) { // error
				response.setError("Failed to create SubscriptionService entry");
				return response;
			}

			// success
			response.setCustomerID(customer.getId());
			response.setServiceID(result.getId());
			response.setSuccess("SubscriptionRequest completed successfully");
			return response;
		});
	}
}
