package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Stack;


import cps.common.Constants;
import cps.entities.models.CarTransportation;
import cps.entities.models.ParkingLot;
import cps.entities.models.ParkingService;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.devices.Robot;

@SuppressWarnings("unused")
public class CarTransportationController2 extends RequestController {

	public CarTransportationController2(ServerController serverController) {
		super(serverController);
	}

	/** The robots. */
	private Map<Integer, Robot> robots;

	/**
	 * Insert multiply cars. This function should only be run after checking there
	 * is at least one empty space in the parking lot
	 *
	 * @param conn
	 *            the conn
	 * @param lot
	 *            the lot
	 * @param carIds
	 *            Stack holding the car id's
	 * @param exitTimes
	 *            Stack holding the exit time's, correlated with car id's
	 * @return true, if successful
	 * @throws SQLException
	 *             the SQL exception
	 * @throws ServerException
	 *             the server exception
	 */
	private boolean insertCars(Connection conn, ParkingLot lot, Stack<String> carIds, Stack<LocalDateTime> exitTimes)
			throws SQLException, ServerException {
		// Get the parking lot
		String[][][] thisContent = lot.getContentAsArray();
		String carId = null;
		LocalDateTime exitTime = null;
		int lotSize = lot.getSize();

		/*
		 * Lower number represents higher priority - the car will be closer to exit 0
		 * <--> size+3 (7,11,15) We have a priority for each Row and another three for
		 * the time when all the lot is full except the front lines of three Then we
		 * will start to fill them one by one
		 */
		int priority, worstPriority = lotSize + 3;
		int iSize, iHeight, maxSize, maxHeight, maxDepth, path, minPath;
		// Easy count of free spots in each priority
		int[] freeSpotsCount = new int[worstPriority];
		for (int i = 0; i < worstPriority; i++) {
			freeSpotsCount[i] = 0;
		}
		/*
		 * Count priorities from 3 to worst
		 */
		for (iSize = 0; iSize < lotSize; iSize++) {
			for (iHeight = 0; iHeight < 5; iHeight++)
				if (thisContent[iSize][Math.floorMod(iHeight, 3)][Math.floorDiv(iHeight, 3) + 1]
						.equals(Constants.SPOT_IS_EMPTY)) {
					freeSpotsCount[iSize + 3]++;
				}
		}
		/*
		 * count priorities 0,1,2
		 */
		for (iHeight = 0; iHeight < 3; iHeight++) {
			for (iSize = lotSize; iSize >= 0; iSize++) {
				if (thisContent[iSize][iHeight][0].equals(Constants.SPOT_IS_EMPTY)) {
					freeSpotsCount[iHeight]++;
				}
			}
		}

		/*
		 * This algorithm will find parking spots for the cars
		 */

		while (!carIds.isEmpty()) {
			carId = carIds.pop();
			exitTime = exitTimes.pop();

			priority = worstPriority;
			/*
			 * Optimal coordinates for insertion These are selected based on the lowest
			 * "cars in the way" number
			 */
			maxSize = -1;
			maxHeight = -1;
			maxDepth = -1;

			// How much cars are in the way in this spot
			path = 100;
			// What is the lowest number of "cars in the way" that we encountered so far
			minPath = 100;

			priority = calculatePriority(exitTime, worstPriority, lotSize);

			// Find spot for the car, based on priority
			iSize = priority;
			/*
			 * Seek a place among this and worse priorities
			 */
			for (iSize = priority; (iSize >= worstPriority) && (maxSize == -1); iSize++) {
				if (freeSpotsCount[iSize] != 0) {
					// If there is at least one free space here find it
					for (iHeight = 5; (iHeight > 0) && (maxSize == -1); iHeight--) {
						if (thisContent[iSize][Math.floorMod(iHeight, 3)][Math.floorDiv(iHeight, 3) + 1]
								.compareTo(Constants.SPOT_IS_EMPTY) == 0) {
							maxSize = iSize;
							maxHeight = Math.floorMod(iHeight, 3);
							maxDepth = Math.floorDiv(iHeight, 3) + 1;
						}
					}
				} else {
					// if there are no free spaces here, try to promote someone
					int otherPriority;
					String[] carInfo = null;
					// For every car in "priority" block
					for (iHeight = 5; (iHeight > 0) && (maxSize == -1); iHeight--) {
						// Parse the car info
						carInfo = thisContent[iSize][Math.floorMod(iHeight, 3)][Math.floorDiv(iHeight, 3) + 1]
								.split(";");
						// Calculate the priority
						otherPriority = calculatePriority(LocalDateTime.parse(carInfo[1]), worstPriority, lotSize);
						/*
						 * If this one can be promoted, try to find a place for him in higher priorities
						 */
						if (otherPriority < iSize) {
							for (int i = iSize - 1; (i < 2) && (maxSize == -1); i--) {
								// If there is a spot for him - mark his current spot as a spot for our
								// insertion
								if (freeSpotsCount[i] != 0) {
									maxSize = iSize;
									maxHeight = Math.floorMod(iHeight, 3);
									maxDepth = Math.floorDiv(iHeight, 3) + 1;
								}
							}
						}
					}
				}
			}
			/*
			 * if no spot was found among worse priorities, start rising up
			 */
			for (iSize = priority - 1; (iSize < 2) && (maxSize == -1); iSize--) {
				if (freeSpotsCount[iSize] != 0) {
					// If there is at least one free space here find it
					for (iHeight = 5; (iHeight > 0) && (maxSize == -1); iHeight--) {
						if (thisContent[iSize][Math.floorMod(iHeight, 3)][Math.floorDiv(iHeight, 3) + 1]
								.compareTo(Constants.SPOT_IS_EMPTY) == 0) {
							maxSize = iSize;
							maxHeight = Math.floorMod(iHeight, 3);
							maxDepth = Math.floorDiv(iHeight, 3) + 1;
						}
					}
				}
			}

			for (iHeight = 2; (iHeight >= 0) && (maxHeight == -1); iHeight--) {
				for (iSize = lotSize - 1; ((iSize >= 0) && (maxSize == -1)); iSize--) {
					if (thisContent[iSize][iHeight][0].compareTo(Constants.SPOT_IS_EMPTY) == 0) {
						maxSize = iSize;
						maxHeight = iHeight;
						maxDepth = 0;
					}
				}
			}
			/*
			 * for (iSize = 0; iSize < lotSize; iSize++) { for (iHeight = 0; iHeight <
			 * priority + 1; iHeight++) { if ((thisContent[iSize][iHeight][priority -
			 * iHeight]) .compareTo(Constants.SPOT_IS_EMPTY) == 0) { path =
			 * calculatePath(thisContent, iSize, iHeight, priority - iHeight); if (path <
			 * minPath) { minPath = path; maxSize = iSize; maxHeight = iHeight; maxDepth =
			 * priority - iHeight; } } } }
			 */

			// insert the car
			System.out.println(
					String.format("Inserting car %s into location %s, %s, %s", carId, maxSize, maxHeight, maxDepth));
			thisContent[maxSize][maxHeight][maxDepth] = String.format("%s;%s", carId, exitTime.toString());
			// call a robot
			// this.robots.get(Integer.parseInt(lot.getRobotIP())).insertCar(carId,
			// maxSize,maxHeight, maxDepth);
		}

		lot.setContentFromArray(thisContent);
		lot.update(conn);

		return true;

	}

	/**
	 * Attempt to insert the car into the lot. Optimal coordinates are calculated
	 * before insertion If the lot is full, or some other error occurs,
	 * LotController will return an appropriate error response, which we will send
	 * back to the user
	 *
	 * @param conn
	 *            the conn
	 * @param lot
	 *            the lot
	 * @param carId
	 *            the car id
	 * @param exitTime
	 *            the exit time
	 * @return the server response
	 * @throws SQLException
	 *             the SQL exception
	 * @throws ServerException
	 *             the server exception
	 * @throws CarTransportationException
	 *             the car transportation exception
	 */
	public void insertCar(Connection conn, ParkingLot lot, String carId, LocalDateTime exitTime)
			throws SQLException, ServerException, CarTransportationException {
		Stack<String> carIds = new Stack<String>();
		carIds.push(carId);

		Stack<LocalDateTime> exitTimes = new Stack<LocalDateTime>();
		exitTimes.push(exitTime);

		// check if there is free space at all
		if (lot.getFreeSpotsNumber() <= 0) {
			throw new CarTransportationException("No free space in the parking lot!");
		}

		if (!insertCars(conn, lot, carIds, exitTimes)) {
			throw new CarTransportationException("Car Insertion failed");
		}
	}

	/**
	 * This function will calculate how much cars we need to move in order to insert
	 * a car to the specified spot.
	 *
	 * @param content
	 *            Lot content as array
	 * @param iSize
	 *            X coordinate of the spot
	 * @param iHeight
	 *            Y coordinate of the spot
	 * @param iDepth
	 *            Z coordinate of the spot
	 * @return Number of cars that are in the way of the desired car insertion
	 */
	private int calculatePath(String[][][] content, int iSize, int iHeight, int iDepth) {
		// System.out.println(String.format("calculatePath(content, %s, %s, %s)", iSize,
		// iHeight, iDepth));
		int totalcars = 0;
		int h = iHeight, d = iDepth;

		while (d > 0) {
			d--;
			if (!content[iSize][h][d].equals(Constants.SPOT_IS_EMPTY)) {
				totalcars++;
			}
		}

		while (h > 0) {
			h--;
			if (!content[iSize][h][d].equals(Constants.SPOT_IS_EMPTY)) {
				totalcars++;
			}
		}

		return totalcars;
	}

	/**
	 * Retrieve car.
	 *
	 * @param conn
	 *            the conn
	 * @param lotId
	 *            the lot id
	 * @param carID
	 *            the car ID
	 * @return the server response
	 * @throws SQLException
	 *             the SQL exception
	 * @throws CarTransportationException
	 */
	public void retrieveCar(Connection conn, int lotId, String carID)
			throws SQLException, CarTransportationException, ServerException {
		ParkingLot lot = ParkingLot.findByID(conn, lotId);
		String[][][] content = lot.getContentAsArray();

		// Get the robot in the parking lot
		/*
		 * Robot robbie = robots.get(Integer.parseInt(lot.getRobotIP())); if (robbie ==
		 * null) { return false; }
		 */

		int eSize = -1, eHeight = -1, eDepth = -1;
		boolean found = false;

		// Find a car in the lot by carId
		for (int iSize = 0; iSize < lot.getSize() && !found; iSize++) {
			for (int iHeight = 0; iHeight < 3 && !found; iHeight++) {
				for (int iDepth = 0; iDepth < 3 && !found; iDepth++) {
					if (content[iSize][iHeight][iDepth].equals(carID)) {
						// When found, mark the spot as empty and remember the location
						eSize = iSize;
						eHeight = iHeight;
						eDepth = iDepth;

						content[iSize][iHeight][iDepth] = Constants.SPOT_IS_EMPTY;

						found = true;
					}
				}
			}
		}

		System.out.println(String.format("Retrieving car %s from location %s, %s, %s", carID, eSize, eHeight, eDepth));

		/*
		 * We need to remove all the cars in the way before we will be able to
		 * "retrieve the car"
		 */

		Stack<String> carIds = new Stack<String>();
		Stack<LocalDateTime> exitTimes = new Stack<LocalDateTime>();

		for (int iHeight = 0; iHeight < eHeight; iHeight++) {
			String currentSlot = content[eSize][iHeight][0];

			if (!currentSlot.equals(Constants.SPOT_IS_EMPTY)) {
				carIds.push(currentSlot);

				CarTransportation transportation = CarTransportation.findByCarId(conn, currentSlot, lotId);
				ParkingService service = transportation.getParkingService(conn);
				exitTimes.push(service.getExitTime());

				content[eSize][iHeight][0] = Constants.SPOT_IS_EMPTY;
				// robbie.retrieveCar(carIds.peek(), eSize, iHeight, 0);
			}
		}

		for (int iDepth = 0; iDepth < eDepth; iDepth++) {
			String currentSlot = content[eSize][eHeight][iDepth];

			if (!currentSlot.equals(Constants.SPOT_IS_EMPTY)) {
				carIds.push(currentSlot);

				CarTransportation transportation = CarTransportation.findByCarId(conn, currentSlot, lotId);
				ParkingService service = transportation.getParkingService(conn);
				exitTimes.push(service.getExitTime());

				content[eSize][eHeight][iDepth] = Constants.SPOT_IS_EMPTY;
				// robbie.retrieveCar(carIds.peek(), eSize, iHeight, iDepth);
			}
		}

		// robbie.retrieveCar(carID, eSize, iHeight, iDepth);
		if (!insertCars(conn, lot, carIds, exitTimes)) {
			throw new CarTransportationException("Failed to retrieve car");
		}
	}

	private int calculatePriority(LocalDateTime exitTime, int worstPriority, int lotSize) {
		int priority = 0;
		LocalDateTime fullSubscriptionTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		/*
		 * Calculate exit priority if there is no exit time - it means we deal with FULL
		 * SUBSCRIPTION which will perhaps stay for long hours or even more than one day
		 */
		if (exitTime == null || exitTime.equals(fullSubscriptionTime)) {
			// assign worst priority
			priority = worstPriority;
		} else {
			// if more than two days - worst priority
			if (exitTime.isAfter(LocalDateTime.now().plusDays(2))) {
				priority = worstPriority;
			} else {
				/*
				 * if less that two days divide minutes until exit by minutes in two days it
				 * will give us a number between 0 and 1 multiply by size and add 3 this means
				 * that we will attempt to inserts the car further back before trying to put it
				 * somewhere in the way of transportation
				 */
				priority = (int) (3 + (lotSize) * (LocalDateTime.now().until(exitTime, ChronoUnit.MINUTES))
						/ (LocalTime.MAX.toSecondOfDay() * 2 / 60));
				// if there was an error in calculations we want to know for debug
				if (priority > worstPriority || priority < 0) {
					System.out.printf("Error in calculations!!! Priority = %d", priority);
					// fix infinite loop
					priority = worstPriority;
				}
			}
		}

		return priority;
	}

}
