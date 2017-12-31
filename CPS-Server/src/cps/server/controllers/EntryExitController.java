package cps.server.controllers;

import cps.api.request.ParkingEntryRequest;
import cps.api.request.ParkingExitRequest;
import cps.api.response.ServerResponse;
import cps.common.Utilities.Holder;
import cps.entities.models.CarTransportation;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.server.ServerApplication;

// TODO: Auto-generated Javadoc
/**
 * The Class EntryExitController.
 */
public class EntryExitController extends RequestController {
	
	/**
	 * Instantiates a new entry exit controller.
	 *
	 * @param serverApplication the server application
	 */
	public EntryExitController(ServerApplication serverApplication) {
		super(serverApplication);
	}

	/**
	 * Handle.
	 *
	 * @param request the request
	 * @return the server response
	 */
	public ServerResponse handle(ParkingEntryRequest request) {
		ServerResponse response = null;

		if (request.getSubscriptionID() == 0) { // The customer wants to enter based on car ID only - check if they have
												// a OnetimeService entry
			response = handleOnetimeEntry(request);
		} else { // The customer wants to enter based on car ID and subscription ID - check if
					// they have a SubscriptionService entry
			response = handleSubscriptionEntry(request);
		}

		return response == null ? ServerResponse.error("The ParkingEntry request was not handled properly") : response;
	}

	/**
	 * Handle subscription entry.
	 *
	 * @param request the request
	 * @return the server response
	 */
	private ServerResponse handleSubscriptionEntry(ParkingEntryRequest request) {
		// TODO: implement
		return null;
	}

	/**
	 * Handle onetime entry.
	 *
	 * @param request the request
	 * @return the server response
	 */
	private ServerResponse handleOnetimeEntry(ParkingEntryRequest request) {
		// Response holder - the lambda function will save the ServerResponse in this
		// object, and then we will retrieve the response and send it back to the user.
		Holder<ServerResponse> responseHolder = new Holder<ServerResponse>(null);

		// Shortcuts for commonly used properties
		int customerID = request.getCustomerID();
		String carID = request.getCarID();
		int lotID = request.getLotID();

		databaseController.performAction(conn -> {
			// Find the OnetimeService that gives this customer an entry license.
			// If the OnetimeService with the right parameters exists, then they can park
			// their car in specified parking lot.
			OnetimeService service = OnetimeService.findForEntry(conn, request.getCustomerID(), request.getCarID(),
					request.getLotID());

			if (service == null) { // Entry license does not exist
				responseHolder.setValue(ServerResponse
						.error("Entry license not found for customer ID " + customerID + " with car ID " + carID));
				return;
			}

			// Entry license exists - continue
			// Find the parking lot that the customer wants to enter
			ParkingLot lot = ParkingLot.findByID(conn, lotID);

			if (lot == null) { // Lot does not exist
				responseHolder.setValue(ServerResponse.error("Parking Lot with ID " + lotID + " does not exist"));
				return;
			}

			// Attempt to insert the car into the lot.
			// Optimal coordinates are calculated before insertion.
			// If the lot is full, or some other error occurs, LotController will return an
			// appropriate error response, which we will send back to the user.
			LotController lotController = serverApplication.getLotController();
			ServerResponse lotControllerResponse = lotController.insertCar(lot, carID);

			if (lotControllerResponse != null) { // Car insertion failed - lot full or some other error
				responseHolder.setValue(lotControllerResponse);
				return;
			}

			// All good - create a CarTransportation table entry to record the fact that a
			// successful parking was made.
			CarTransportation transportation = CarTransportation.create(conn, request.getCustomerID(),
					request.getCarID(), OnetimeService.TYPE, service.getId(), request.getLotID());

			if (transportation == null) { // Failed to create a CarTransportation entry - this normally shouldn't happen
				responseHolder.setValue((ServerResponse.error("CarTransportation entry creation failed")));
				return;
			}

			responseHolder.setValue(ServerResponse.ok("ParkingEntry request completed successfully"));
		});

		return responseHolder.getValue();
	}

	/**
	 * Handle.
	 *
	 * @param request the request
	 * @return the server response
	 */
	public ServerResponse handle(ParkingExitRequest request) {
		Holder<CarTransportation> result = new Holder<>(null);
		databaseController.performAction(conn -> {
			// TODO: implement
		});

		// System.out.println(result.getValue());
		return ServerResponse.decide("Entry creation", result.getValue() != null);
	}
}
