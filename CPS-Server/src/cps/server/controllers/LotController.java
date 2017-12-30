package cps.server.controllers;

import java.util.Map;

import cps.api.action.InitLotAction;
import cps.api.response.ServerResponse;
import cps.common.Utilities.Holder;
import cps.server.ServerApplication;
import cps.server.devices.Robot;
import cps.entities.models.ParkingLot;

// TODO: Auto-generated Javadoc
/**
 * The Class LotController.
 */
public class LotController extends RequestController {
	
	/**
	 * Instantiates a new lot controller.
	 *
	 * @param serverApplication serverApplication object
	 */
	public LotController(ServerApplication serverApplication) {
		super(serverApplication);
	}

	/** The robots. */
	@SuppressWarnings("unused")
	private Map<Integer, Robot> robots;
	
	/**
	 * Insert car.
	 *
	 * @param lot the lot
	 * @param carID the car ID
	 * @return the server response
	 */
	public ServerResponse insertCar(ParkingLot lot, String carID) {
		return null; // return null if no error
	}
	
	/**
	 * Retrieve car.
	 *
	 * @param lot the lot
	 * @param carID the car ID
	 * @return the server response
	 */
	public ServerResponse retrieveCar(ParkingLot lot, String carID) {
		return null; // return null if no error
	}
	
	/**
	 * Handle.
	 *
	 * @param request the request
	 * @return the server response
	 */
	public ServerResponse handle(InitLotAction request) {
		Holder<ParkingLot> result = new Holder<>(null);
		databaseController.performAction(conn -> {
			result.setValue(ParkingLot.create(conn, request.getStreetAddress(), request.getSize(), request.getPrice1(), request.getPrice2(), request.getRobotIP()));
		});

		return ServerResponse.decide("Entry creation", result.getValue() != null);	
	}
}
