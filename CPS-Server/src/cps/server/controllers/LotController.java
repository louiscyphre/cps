package cps.server.controllers;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Map;
import cps.common.*;
import cps.api.action.InitLotAction;
import cps.api.response.InitLotResponse;
import cps.api.response.ServerResponse;
import cps.server.ServerController;
import cps.server.devices.Robot;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.entities.models.ParkingService;
import cps.entities.models.SubscriptionService;

/**
 * The Class LotController.
 */
public class LotController extends RequestController {

	/**
	 * Instantiates a new lot controller.
	 *
	 * @param serverController
	 *            serverApplication object
	 */
	public LotController(ServerController serverController) {
		super(serverController);
	}

	/** The robots. */
	@SuppressWarnings("unused")
	private Map<Integer, Robot> robots;

	/**
	 * Attempt to insert the car into the lot. Optimal coordinates are calculated
	 * before insertion If the lot is full, or some other error occurs,
	 * LotController will return an appropriate error response, which we will send
	 * back to the user
	 *
	 *
	 * @param lot
	 *            the lot
	 * @param carID
	 *            the car ID
	 * @return the server response
	 */
	public boolean insertCar(ParkingLot lot, ParkingService service, ServerResponse response) {
		// TODO: calculate optimal coordinates and call Robot::insertCar
		// Get the parking lot
		String[][][] thisContent = lot.getContentAsArray();
		OnetimeService a = null;
		SubscriptionService b = null;
		LocalTime _exitTime = null;
		// Lower number represents higher priority - the car will be closer to exit
		int priority = 0;
		int iSize,iHeight,iDepth;
		// extract exit time
		if (service.getLicenseType() == Constants.LICENSE_TYPE_ONETIME) {
			a = (OnetimeService) service;
			_exitTime = a.getPlannedEndTime().toLocalDateTime().toLocalTime();
		} else {
			b = (SubscriptionService) service;
			_exitTime = b.getDailyExitTime();
		}
		// if there is no exit time - it means we deal with full subscription which will
		// perhaps stay for long hours or even more than one day
		if (_exitTime == null || _exitTime.equals(LocalTime.MIDNIGHT)) {
			//assign lowest priority
			priority = 9;
		}
		
		
		
		// call robot
		return true;
	}

	/**
	 * Retrieve car.
	 *
	 * @param lot
	 *            the lot
	 * @param carID
	 *            the car ID
	 * @return the server response
	 */
	public boolean retrieveCar(ParkingLot lot, String carID, ServerResponse response) {
		return true; // TODO: find the coordinates and call Robot::retrieveCar
	}

	/**
	 * Handle InitLotAction.
	 *
	 * @param request
	 *            the request
	 * @return the server response
	 */
	public InitLotResponse handle(InitLotAction request) {
		return databaseController.performQuery(conn -> {
			ParkingLot result = ParkingLot.create(conn, request.getStreetAddress(), request.getSize(),
					request.getPrice1(), request.getPrice2(), request.getRobotIP());

			if (result == null) {
				return new InitLotResponse(false, "Failed to create parking lot", 0);
			}

			return new InitLotResponse(true, "Parking lot created successfully", result.getId());
		});
	}
}
