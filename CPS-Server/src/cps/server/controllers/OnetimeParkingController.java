/*
 * 
 */
package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import cps.api.request.IncidentalParkingRequest;
import cps.api.request.ListOnetimeEntriesRequest;
import cps.api.request.ReservedParkingRequest;
import cps.api.response.ListOnetimeEntriesResponse;
import cps.api.response.ServerResponse;
import cps.entities.models.Customer;
import cps.entities.models.OnetimeService;
import cps.server.ServerApplication;
import cps.server.ServerController;
import cps.common.Utilities.Holder;
import cps.common.Utilities.Pair;

/**
 * The Class OnetimeParkingController.
 */
@SuppressWarnings("unused")
public class OnetimeParkingController extends RequestController {	
	
	/**
	 * Instantiates a new one-time parking controller.
	 *
	 * @param serverController the server application
	 */
	public OnetimeParkingController(ServerController serverController) {
		super(serverController);
	}

	/**
	 * Handle IncidentalParkingRequest.
	 *
	 * @param request the request
	 * @return the server response
	 */
	public ServerResponse handle(IncidentalParkingRequest request) {
		return databaseController.performQuery(conn -> {
			// TODO: finish customer login/registration for this to work
//			Pair<Customer, ServerResponse> customerExists = serverController.getUserController().findOrCreateCustomer(conn, request.getCustomerID());
//			
//			if (customerExists.b != null) {
//				return customerExists.b;
//			}
			
			Timestamp startTime = new Timestamp(System.currentTimeMillis());
			Timestamp plannedEndTime = Timestamp.valueOf(request.getPlannedEndTime());
			OnetimeService result = OnetimeService.create(conn, request.getParkingType(), request.getCustomerID(),
					request.getEmail(), request.getCarID(), request.getLotID(), startTime,
					plannedEndTime, false);
			// System.out.println(result.getValue());
			return ServerResponse.decide("Entry creation", result != null);	
		});
	}
	
	/**
	 * Handle ReservedParkingRequest.
	 *
	 * @param request the request
	 * @return the server response
	 */
	public ServerResponse handle(ReservedParkingRequest request) {
		return databaseController.performQuery(conn -> {
			Timestamp startTime = Timestamp.valueOf(request.getPlannedStartTime());
			Timestamp plannedEndTime = Timestamp.valueOf(request.getPlannedEndTime());
			OnetimeService result = OnetimeService.create(conn, request.getParkingType(), request.getCustomerID(),
					request.getEmail(), request.getCarID(), request.getLotID(), startTime,
					plannedEndTime, false);
			// System.out.println(result.getValue());	
			return ServerResponse.decide("Entry creation", result != null);	
		});
	}
	
	/**
	 * Handle.
	 *
	 * @param request the request
	 * @return the server response
	 */
	public ServerResponse handle(ListOnetimeEntriesRequest request) {
		Holder<Collection<OnetimeService>> result = new Holder<>(null);
		databaseController.performAction(conn -> result.setValue(OnetimeService.findByCustomerID(conn, request.getCustomerID())));		
		System.out.println(result.getValue());
		
		if (result.getValue() == null) {
			return ServerResponse.error("Entry retrieval failed");
		}
		
		return new ListOnetimeEntriesResponse("Entry retrieval successful", result.getValue(), request.getCustomerID());	
	}
}
