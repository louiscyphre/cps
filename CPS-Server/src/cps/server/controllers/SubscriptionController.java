package cps.server.controllers;

import cps.api.response.*;
import cps.common.Constants;
import cps.entities.models.Customer;
import cps.entities.models.ParkingLot;
import cps.entities.models.SubscriptionService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import cps.api.request.FullSubscriptionRequest;
import cps.api.request.RegularSubscriptionRequest;
import cps.api.request.SubscriptionRequest;
import cps.server.ServerException;
import cps.server.ServerController;
import cps.server.session.CustomerSession;
import cps.server.session.UserSession;

public class SubscriptionController extends RequestController {

	public SubscriptionController(ServerController serverController) {
		super(serverController);
	}
	
	public ServerResponse handle(FullSubscriptionRequest request, UserSession session) {
		LocalDate startDate = request.getStartDate();
		LocalDate endDate = startDate.plusDays(28);
		LocalTime dailyExitTime = LocalTime.of(0, 0, 0);
		FullSubscriptionResponse response = new FullSubscriptionResponse(false, "");
		return handle(request, response, startDate, endDate, dailyExitTime);
	}
	
	public ServerResponse handle(RegularSubscriptionRequest request, UserSession session) {
		LocalDate startDate = request.getStartDate();
		LocalDate endDate = startDate.plusDays(28);
		LocalTime dailyExitTime = request.getDailyExitTime();
		RegularSubscriptionResponse response = new RegularSubscriptionResponse(false, "");
		return handle(request, response, startDate, endDate, dailyExitTime);
	}
	
	public ServerResponse handle(SubscriptionRequest request, SubscriptionResponse response, LocalDate startDate, LocalDate endDate, LocalTime dailyExitTime) {
		return databaseController.performQuery(conn -> {			
			// TODO check request parameters
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
			
			try {				
				// Calculate payment
				float payment = paymentForSubscription(conn, customer, result);

				// Write payment
				customer.pay(conn,  payment);

				// Success
				response.setCustomerID(customer.getId());
				response.setServiceID(result.getId());
				response.setPayment(payment);
				response.setSuccess("SubscriptionRequest completed successfully");
			} catch (ServerException ex) {
				response.setError(ex.getMessage());
			}
			
			return response;
		});
	}

	private float paymentForSubscription(Connection conn, Customer customer, SubscriptionService service) throws SQLException {		
		if (service.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_REGULAR) {
			// Regular monthly subscription
			ParkingLot lot = ParkingLot.findByID(conn, service.getLotID());			
			float pricePerHour = lot.getPrice2();
			
			int numCars = SubscriptionService.countForCustomer(conn, customer.getId(), service.getSubscriptionType());
			
			if (numCars > 1) {
				return pricePerHour * 54f;
			}
			
			return pricePerHour * 60f;
		} else {
			// Full monthly subscription
			float pricePerHour = Constants.PRICE_PER_HOUR_RESERVED;
			return pricePerHour * 72f;
		}
	}
}
