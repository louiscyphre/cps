package cps.server.controllers;

import java.util.Map;

import cps.api.action.InitLotAction;
import cps.api.response.ServerResponse;
import cps.common.Utilities.Holder;
import cps.server.ServerApplication;
import cps.server.ServerController;
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
	 * @param serverController serverApplication object
	 */
	public LotController(ServerController serverController) {
		super(serverController);
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
		return null; // TODO: calculate optimal coordinates and call Robot::insertCar
	}
	
	/**
	 * Retrieve car.
	 *
	 * @param lot the lot
	 * @param carID the car ID
	 * @return the server response
	 */
	public ServerResponse retrieveCar(ParkingLot lot, String carID) {
		return null; // TODO: find the coordinates and call Robot::retrieveCar
	}
	
	/**
	 * Handle InitLotAction.
	 *
	 * @param request the request
	 * @return the server response
	 */
	public ServerResponse handle(InitLotAction request) {
		return databaseController.performQuery(conn -> {
			ParkingLot result = ParkingLot.create(conn, request.getStreetAddress(), request.getSize(), request.getPrice1(), request.getPrice2(), request.getRobotIP());
			return ServerResponse.decide("Entry creation", result != null);
		});
	}
}
