package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import cps.api.request.ParkingExitRequest;
import cps.api.response.ParkingExitResponse;
import cps.api.response.ServerResponse;
import cps.common.*;
import cps.entities.models.CarTransportation;
import cps.entities.models.Customer;
import cps.entities.models.DatabaseException;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.server.ServerController;

/**
 * The Class EntryExitController.
 */
public class ParkingExitController extends RequestController {

	/**
	 * Instantiates a new entry exit controller.
	 *
	 * @param serverController
	 *            the server application
	 */
	public ParkingExitController(ServerController serverController) {
		super(serverController);
	}

	/**
	 * Handle ParkingExitRequest.
	 *
	 * @param request
	 *            the request
	 * @return the server response
	 */
	public ServerResponse handle(ParkingExitRequest request) {
		return databaseController.performQuery(conn -> {
			ParkingExitResponse response = new ParkingExitResponse(false, "");

			CarTransportation entry = CarTransportation.findForExit(conn, request.getCustomerID(), request.getCarID(),
					request.getLotID());

			if (entry == null) { // CarTransportation does not exist
				response.setError("CarTransportation with the requested parameters does not exist");
				return response;
			}

			// Update the removedAt field
			Timestamp removedAt = new Timestamp(System.currentTimeMillis());

			if (!entry.updateRemovedAt(conn, removedAt)) {
				response.setError("Failed to update car transportation");
				return response;
			}
			
			try {
				// Calculate the amount of money that the customer has to pay
				float sum = calculatePayment(conn, entry, request);

				// Find customer
				Customer customer = Customer.findByIDNotNull(conn, request.getCustomerID());

				// Write payment
				customer.pay(conn, sum);
				
				/*
				 * Attempt to retrieve the car from the lot The function will shuffle the cars
				 * that get in the way in an attempt to locate them in accordance to the current
				 * situation
				 */
				CarTransportationController transportationController = serverController.getTransportationController();
				transportationController.retrieveCar(conn, request.getLotID(), request.getCarID()); // TODO wait until this is fixed

				// Success
				response.setCustomerID(customer.getId());
				response.setPayment(sum);
				response.setSuccess("ParkingExit request completed successfully");
			} catch (DatabaseException | CarTransportationException ex) {
				response.setError(ex.getMessage());
			}
			
			return response;
		});
	}

	public static float calculatePayment(Connection conn, CarTransportation carTransportation,
			ParkingExitRequest exitRequest) throws SQLException {
		float tariffLow = 0, tariffHigh = 0;

		long startedLate, inside, endedLate;

		// Determine if this is a subscription or one time
		if (carTransportation.getAuthType() == Constants.LICENSE_TYPE_ONETIME) {
			// If customer did not park by subscription charge him according to park type
			// Determine incidental or reserved parking
			OnetimeService parkingService = OnetimeService.findByID(conn, carTransportation.getAuthID());

			// Determine prices at that parking lot
			ParkingLot parkingLot = ParkingLot.findByID(conn, exitRequest.getLotID());

			if (parkingService.getParkingType() == Constants.PARKING_TYPE_INCIDENTAL) {
				tariffLow = parkingLot.getPrice1() / 60; // get incidental price
				tariffHigh = tariffLow / 60; // fine price equals regular price
			} else {
				tariffLow = parkingLot.getPrice2() / 60; // get reserved price
				tariffHigh = parkingLot.getPrice1() / 60; // fine price is higher
			}

			// Was the customer late or early? negative means early positive means late
			startedLate = carTransportation.getInsertedAt().getTime() - parkingService.getPlannedStartTime().getTime();
			startedLate = startedLate / (60 * 1000); // convert to minutes

			// How much time the customer ordered?
			inside = parkingService.getPlannedEndTime().getTime() - parkingService.getPlannedStartTime().getTime();
			inside = inside / (60 * 1000);

			// Has the customer left late or early? negative means early positive means late
			endedLate = carTransportation.getRemovedAt().getTime() - parkingService.getPlannedEndTime().getTime();
			endedLate = endedLate / (60 * 1000);

			// If the customer running late less that 30 minutes - discard being late and
			// charge full ordered time
			if (startedLate < 30) {
				startedLate = (long) 0;
			} else {
				// if the customer came in early simply add minutes to total time and charge
				// normal
				if (startedLate < 0) {
					inside -= startedLate;
				}
			}

			// If the customer exits early - charge full time and discard early minutes
			if (endedLate < 0) {
				endedLate = (long) 0;
			}
			// At last we calculate the sum
			// The formula is according to the requirements
			return startedLate * tariffLow * 1.2f + inside * tariffLow + endedLate * tariffHigh;
		}

		// TODO: if customer has subscription - charge him only for being late
		// TODO: calculate subscription exit late fine

		return 0f;
	}
}
