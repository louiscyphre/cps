package cps.server.controllers;

import java.sql.Timestamp;

import cps.api.request.ParkingEntryRequest;
import cps.api.request.ParkingExitRequest;
import cps.api.response.ServerResponse;
import cps.common.*;
import cps.entities.models.CarTransportation;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.entities.models.SubscriptionService;
import cps.server.ServerController;

// TODO: Auto-generated Javadoc
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
					request.getCarID(), OnetimeService.TYPE, service.getId(), request.getLotID());

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
					request.getCarID(), OnetimeService.TYPE, service.getId(), request.getLotID());

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
			// update the removedAt field
			Timestamp removedAt = new Timestamp(System.currentTimeMillis());
			
			if (0 <= entry.updateRemovedAt(conn, removedAt)) {
				return ServerResponse.error("Failed to update car transportation");
			}
			// TODO: calculate payment - DONE
			// TODO: calculate subscription exit late fine
			Utilities.ChargeCustomer(conn,entry,request);
			
			return ServerResponse.decide("Entry update", entry != null);
		});
	}
}
