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
import cps.api.request.ReservedParkingRequest;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.ListOnetimeEntriesResponse;
import cps.api.response.ServerResponse;
import cps.entities.models.DailyStatistics;
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
	 * @param serverController
	 *            the server application
	 */
	public OnetimeParkingController(ServerController serverController) {
		super(serverController);
	}

	/**
	 * Handle IncidentalParkingRequest.
	 *
	 * @param request
	 *            the request
	 * @return the server response
	 */
	public ServerResponse handle(IncidentalParkingRequest request) {
		return databaseController.performQuery(conn -> {
			// TODO: finish customer login/registration for this to work
			// Pair<Customer, ServerResponse> customerExists =
			// serverController.getUserController().findOrCreateCustomer(conn,
			// request.getCustomerID());
			//
			// if (customerExists.b != null) {
			// return customerExists.b;
			// }

			Timestamp startTime = new Timestamp(System.currentTimeMillis());
			Timestamp plannedEndTime = Timestamp.valueOf(request.getPlannedEndTime());

			OnetimeService result = OnetimeService.create(conn, request.getParkingType(), request.getCustomerID(),
					request.getEmail(), request.getCarID(), request.getLotID(), startTime, plannedEndTime, false);

			if (result == null) { // error
				return new IncidentalParkingResponse(false, request.getCustomerID(), "", 0);
			}

			// success
			return new IncidentalParkingResponse(true, request.getCustomerID(), "", result.getId());
		});
	}

	/**
	 * Handle ReservedParkingRequest.
	 *
	 * @param request
	 *            the request
	 * @return the server response
	 */
	public ServerResponse handle(ReservedParkingRequest request) {
		return databaseController.performQuery(conn -> {
			Timestamp startTime = Timestamp.valueOf(request.getPlannedStartTime());
			Timestamp plannedEndTime = Timestamp.valueOf(request.getPlannedEndTime());

			OnetimeService result = OnetimeService.create(conn, request.getParkingType(), request.getCustomerID(),
					request.getEmail(), request.getCarID(), request.getLotID(), startTime, plannedEndTime, false);
			// System.out.println(result.getValue());

			if (result == null) { // error
				return new IncidentalParkingResponse(false, request.getCustomerID(), "", 0);
			}

			// success
			return new IncidentalParkingResponse(true, request.getCustomerID(), "", result.getId());
		});
	}

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
