/*
 * 
 */
package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import cps.api.request.*;
import cps.api.response.ListOnetimeEntriesResponse;
import cps.api.response.ServerResponse;
import cps.entities.models.DailyStatistics;
import cps.entities.models.OnetimeService;
import cps.server.ServerApplication;
import cps.server.ServerController;
import cps.common.Utilities.Holder;

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
			Timestamp startTime = new Timestamp(System.currentTimeMillis());
			Timestamp plannedEndTime = Timestamp.valueOf(request.getPlannedEndTime());
			OnetimeService result = OnetimeService.create(conn, request.getParkingType(), request.getCustomerID(),
					request.getEmail(), request.getCarID(), request.getLotID(), startTime, plannedEndTime, false);
			// System.out.println(result.getValue());
			return ServerResponse.decide("Entry creation", result != null);
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
			return ServerResponse.decide("Entry creation", result != null);
		});
	}

	public ServerResponse handle(CancelOnetimeParkingRequest request) {
		return databaseController.performQuery(conn -> {
			// Mark Order as canceled
			Timestamp _now;
			OnetimeService toc = OnetimeService.findById(conn, request.getOnetimeServiceID());
			// chance field in the object
			toc.setCanceled(true);
			// Find entry in the db and update it to match all the fields
			toc.Update(conn);
			// TODO:i'm here
			// Increase daily counter of canceled orders
			DailyStatistics.IncreaseCanceledOrder(conn, toc.getLotID());

			// Give customer all/some money back

			// Return Server Response
			return ServerResponse.decide("123", true);
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
		Holder<Collection<OnetimeService>> result = new Holder<>(null);
		databaseController
				.performAction(conn -> result.setValue(OnetimeService.findByCustomerID(conn, request.getCustomerID())));
		System.out.println(result.getValue());

		if (result.getValue() == null) {
			return ServerResponse.error("Entry retrieval failed");
		}

		return new ListOnetimeEntriesResponse("Entry retrieval successful", result.getValue(), request.getCustomerID());
	}
}
