/*
 * 
 */
package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import cps.api.request.CancelOnetimeParkingRequest;
import cps.api.request.IncidentalParkingRequest;
import cps.api.request.ListOnetimeEntriesRequest;
import cps.api.request.OnetimeParkingRequest;
import cps.api.request.ReservedParkingRequest;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.ListOnetimeEntriesResponse;
import cps.api.response.OnetimeParkingResponse;
import cps.api.response.ReservedParkingResponse;
import cps.api.response.ServerResponse;
import cps.entities.models.DailyStatistics;
import cps.entities.models.Customer;
import cps.entities.models.OnetimeService;
import cps.server.ServerApplication;
import cps.server.ServerController;
import cps.server.handlers.CustomerSession;
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
	 * @param serverController
	 *            the server application
	 */
	public OnetimeParkingController(ServerController serverController) {
		super(serverController);
	}
	
	public ServerResponse handle(OnetimeParkingRequest request, OnetimeParkingResponse response, Timestamp startTime, Timestamp plannedEndTime) {
		return databaseController.performQuery(conn -> {			
			// TODO: check request parameters
			CustomerSession session = new CustomerSession();
			session.findOrRegisterCustomer(conn, response, request.getCustomerID(), request.getEmail());
			
			Customer customer = session.getCustomer(); // By this time customer has to exist
			
			if (customer == null) {
			 	return response; // the response was modified by the customer session
			}

			OnetimeService result = OnetimeService.create(conn, request.getParkingType(), customer.getId(),
					request.getEmail(), request.getCarID(), request.getLotID(), startTime, plannedEndTime, false);

			if (result == null) { // error
				response.setError("Failed to create OnetimeService entry");
				return response;
			}

			// success
			response.setCustomerID(customer.getId());
			response.setServiceID(result.getId());
			response.setSuccess("OnetimeParkingRequest completed successfully");
			return response;
		});
	}

	/**
	 * Handle IncidentalParkingRequest.
	 *
	 * @param request
	 *            the request
	 * @return the server response
	 */
	public ServerResponse handle(IncidentalParkingRequest request) {
		Timestamp startTime = new Timestamp(System.currentTimeMillis());
		Timestamp plannedEndTime = Timestamp.valueOf(request.getPlannedEndTime());
		IncidentalParkingResponse response = new IncidentalParkingResponse(false, "");
		return handle(request, response, startTime, plannedEndTime);
	}

	/**
	 * Handle ReservedParkingRequest.
	 *
	 * @param request
	 *            the request
	 * @return the server response
	 */
	public ServerResponse handle(ReservedParkingRequest request) {
		Timestamp startTime = Timestamp.valueOf(request.getPlannedStartTime());
		Timestamp plannedEndTime = Timestamp.valueOf(request.getPlannedEndTime());
		ReservedParkingResponse response = new ReservedParkingResponse(false, "");
		return handle(request, response, startTime, plannedEndTime);
	}

	/**
	 * Handle CancelOnetimeParkingRequest.
	 *
	 * @param request
	 *            the request
	 * @return the server response
	 */
	public ServerResponse handle(CancelOnetimeParkingRequest request) {
		return databaseController.performQuery(conn -> {
			// Mark Order as canceled
			Timestamp now;
			OnetimeService serviceToCancel = OnetimeService.findById(conn, request.getOnetimeServiceID());
			// chance field in the object
			serviceToCancel.setCanceled(true);
			// Find entry in the db and update it to match all the fields
			serviceToCancel.update(conn);
			// Increase daily counter of canceled orders
			DailyStatistics.increaseCanceledOrder(conn, serviceToCancel.getLotID());

			// TODO: Cauchy Give customer all/some money back

			// Return Server Response
			return ServerResponse.ok("Order canceled successfully");
		});
	}

	/**
	 * Handle List One Time Entries Request.
	 *
	 * @param request
	 *            the request
	 * @return the server response
	 */
	public ServerResponse handle(ListOnetimeEntriesRequest request) {
		Collection<OnetimeService> result = databaseController
				.performQuery(conn -> OnetimeService.findByCustomerID(conn, request.getCustomerID()));

		return new ListOnetimeEntriesResponse(result, request.getCustomerID());
	}
}
