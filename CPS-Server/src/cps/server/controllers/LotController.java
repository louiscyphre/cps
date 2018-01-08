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
	public boolean insertCar(ParkingLot lot, String carId, LocalTime exitTime, ServerResponse response) {
		// TODO: calculate optimal coordinates and call Robot::insertCar
		// Get the parking lot
		String[][][] thisContent = lot.getContentAsArray();
		// Lower number represents higher priority - the car will be closer to exit
		// 0<-->4
		int priority = 0;
		int iSize, iHeight, iDepth, maxSize=0, maxHeight=0, maxDepth=0, path = 4, minPath = 4;

		// check if there is free space at all
		if (freeSpaceCount(lot, thisContent) <= 0) {
			response.setError("No free space in the parking lot!");
			return false;
		}

		// Calculate exit priority
		// if there is no exit time - it means we deal with full subscription which will
		// perhaps stay for long hours or even more than one day
		if (exitTime == null || exitTime.equals(LocalTime.MIDNIGHT)) {
			// assign worst priority
			priority = 4;
		} else {
			// divide difference between now and exit time
			// by the time left today
			// and round down the calculations
			priority = (int) (5 * (exitTime.toSecondOfDay() - LocalTime.now().toSecondOfDay())
					/ (LocalTime.MAX.toSecondOfDay() - LocalTime.now().toSecondOfDay()));
			if (priority == 5) {
				priority = 4;
			}
		}
		// now with priority, find spot for the car

		switch (priority) {
		case 0:
			for (iSize = 0; iSize < lot.getSize(); iSize++) {
				if (thisContent[iSize][0][0] == "0") {
					thisContent[iSize][0][0] = carId;
					maxSize = iSize;
					maxHeight = 0;
					maxDepth = 0;
				}
				//if(iSize)
			}
			break;
		case 1:

			break;
		case 2:

			break;
		case 3:

			break;
		case 4:

			break;

		default:
			break;
		}

		// call a robot
		this.robots.get(Integer.parseInt(lot.getRobotIP())).insertCar(carId, maxSize, maxHeight, maxDepth);

		return true;
	}

	protected int freeSpaceCount(ParkingLot lot, String[][][] content) {
		int iSize, iHeight, iDepth;
		int free = 3 * 3 * lot.getSize();

		for (iSize = 0; iSize < lot.getSize(); iSize++) {
			for (iHeight = 0; iHeight < 3; iHeight++) {
				for (iDepth = 0; iDepth < 3; iDepth++) {
					if (content[iSize][iHeight][iDepth] == "0") {
						free--;
					}
				}
			}
		}

		return free;
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
