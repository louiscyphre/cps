package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import cps.api.request.ParkingEntryRequest;
import cps.api.request.ParkingExitRequest;
import cps.api.response.ServerResponse;
import cps.common.*;
import cps.entities.models.CarTransportation;
import cps.entities.models.Customer;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.entities.models.SubscriptionService;
import cps.server.ServerController;

/**
 * The Class EntryExitController.
 */
public class EntryExitController extends RequestController {

	/**
	 * Instantiates a new entry exit controller.
	 *
	 * @param serverController
	 *            the server application
	 */
	public EntryExitController(ServerController serverController) {
		super(serverController);
	}

	/**
	 * Handle ParkingEntryRequest.
	 *
	 * @param request
	 *            the request
	 * @return the server response
	 */
	public ServerResponse handle(ParkingEntryRequest request) {
		ServerResponse response = null;

		if (request.getSubscriptionID() == 0) { // The customer wants to enter based on car ID only - check if they have
												// a OnetimeService entry
			response = handleOnetimeEntry(request);
		} else {
			// The customer wants to enter based on car ID and subscription ID - check if
			// they have a SubscriptionService entry
			response = handleSubscriptionEntry(request);
		}

		return response == null ? ServerResponse.error("The ParkingEntry request was not handled properly") : response;
	}

	/**
	 * Handle ParkingEntryRequest with a subscription entry license.
	 *
	 * @param request
	 *            the request
	 * @return the server response
	 */
	private ServerResponse handleSubscriptionEntry(ParkingEntryRequest request) {
		return databaseController.performQuery(conn -> {
			// Shortcuts for commonly used properties
			int customerID = request.getCustomerID();
			String carID = request.getCarID();
			int lotID = request.getLotID();
			int subsID = request.getSubscriptionID();

			// Find the OnetimeService that gives this customer an entry license.
			// If the OnetimeService with the right parameters exists, then they can park
			// their car in specified parking lot.
			SubscriptionService service = SubscriptionService.findForEntry(conn, customerID, carID, subsID);

			if (service == null) { // Entry license does not exist
				return ServerResponse
						.error("Entry license not found for customer ID " + customerID + " with car ID " + carID);
			}

			if (service.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_REGULAR && lotID != service.getLotID()) {
				return ServerResponse
						.error(String.format("Requested parking in lotID = %s, but subscription is for lotID = %s",
								lotID, service.getLotID()));
			}

			// Entry license exists - continue
			// Find the parking lot that the customer wants to enter
			ParkingLot lot = ParkingLot.findByID(conn, lotID);

			if (lot == null) { // Lot does not exist
				return ServerResponse.error("Parking Lot with ID " + lotID + " does not exist");
			}

			// Attempt to insert the car into the lot.
			// Optimal coordinates are calculated before insertion.
			// If the lot is full, or some other error occurs, LotController will return an
			// appropriate error response, which we will send back to the user.
			LotController lotController = serverController.getLotController();
			ServerResponse lotControllerResponse = lotController.insertCar(lot, carID);

			if (lotControllerResponse != null) { // Car insertion failed - lot full or some other error
				return lotControllerResponse;
			}

			// All good - create a CarTransportation table entry to record the fact that a
			// successful parking was made.
			CarTransportation transportation = CarTransportation.create(conn, request.getCustomerID(),
					request.getCarID(), OnetimeService.LICENSE_TYPE, service.getId(), request.getLotID());

			if (transportation == null) { // Failed to create a CarTransportation entry - this normally shouldn't happen
				return ServerResponse.error("CarTransportation entry creation failed");
			}

			return ServerResponse.ok("ParkingEntry request completed successfully");
		});
	}

	/**
	 * Handle ParkingEntryRequest with a one-time entry license.
	 *
	 * @param request
	 *            the request
	 * @return the server response
	 */
	private ServerResponse handleOnetimeEntry(ParkingEntryRequest request) {
		// The lambda function will return a ServerResponse object, which we will send
		// back to the user.
		return databaseController.performQuery(conn -> {
			// Shortcuts for commonly used properties
			int customerID = request.getCustomerID();
			String carID = request.getCarID();
			int lotID = request.getLotID();

			// Find the OnetimeService that gives this customer an entry license.
			// If the OnetimeService with the right parameters exists, then they can park
			// their car in specified parking lot.
			OnetimeService service = OnetimeService.findForEntry(conn, customerID, carID, lotID);

			if (service == null) { // Entry license does not exist
				return ServerResponse
						.error("Entry license not found for customer ID " + customerID + " with car ID " + carID);
			}

			// Entry license exists - continue
			// Find the parking lot that the customer wants to enter
			ParkingLot lot = ParkingLot.findByID(conn, lotID);

			if (lot == null) { // Lot does not exist
				return ServerResponse.error("Parking Lot with ID " + lotID + " does not exist");
			}

			// Attempt to insert the car into the lot.
			// Optimal coordinates are calculated before insertion.
			// If the lot is full, or some other error occurs, LotController will return an
			// appropriate error response, which we will send back to the user.
			LotController lotController = serverController.getLotController();
			ServerResponse lotControllerResponse = lotController.insertCar(lot, carID);

			if (lotControllerResponse != null) { // Car insertion failed - lot full or some other error
				return lotControllerResponse;
			}

			// All good - create a CarTransportation table entry to record the fact that a
			// successful parking was made.
			CarTransportation transportation = CarTransportation.create(conn, request.getCustomerID(),
					request.getCarID(), OnetimeService.LICENSE_TYPE, service.getId(), request.getLotID());

			if (transportation == null) { // Failed to create a CarTransportation entry - this normally shouldn't happen
				return ServerResponse.error("CarTransportation entry creation failed");
			}

			return ServerResponse.ok("ParkingEntry request completed successfully");
		});
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
			CarTransportation entry = CarTransportation.findForExit(conn, request.getCustomerID(), request.getCarID(),
					request.getLotID());

			if (entry == null) { // CarTransportation does not exist
				return ServerResponse.error("CarTransportation with the requested parameters does not exist");
			}
			
			// Update the removedAt field
			Timestamp removedAt = new Timestamp(System.currentTimeMillis());
			
			if (!entry.updateRemovedAt(conn, removedAt)) {
				return ServerResponse.error("Failed to update car transportation");
			}
			
			// Calculate the amount of money that the customer has to pay
			float sum = calculatePayment(conn, entry, request);
			
			// TODO: finish customer login/registration for this to work
			// Find customer
			Customer customer = Customer.findByID(conn, request.getCustomerID());
			
			if (customer == null) {
				return ServerResponse.error("Failed to find customer with id " + request.getCustomerID());
			}
			
			// Write payment
			customer.setDebit(sum + customer.getDebit());
			
			if (!customer.update(conn)) {
				return ServerResponse.error("Failed to update customer");
			}
			
			// Success
			return ServerResponse.ok("ParkingExit request completed successfully");
		});
	}

	public static float calculatePayment(Connection conn, CarTransportation carTransportation,
			ParkingExitRequest exitRequest) throws SQLException {
		float tariffLow = 0, tariffHigh = 0;
		
		long startedLate, inside, endedLate;
		
		// Determine if this is a subscription or one time
		if (carTransportation.getAuthType() == OnetimeService.LICENSE_TYPE) {
			// If customer did not park by subscription charge him according to park type
			// Determine incidental or reserved parking
			OnetimeService parkingService = OnetimeService.findById(conn, carTransportation.getAuthID());

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
