/*
 * 
 */
package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Stack;

import com.google.gson.Gson;

import cps.common.Constants;
import cps.entities.models.ParkingCell;
import cps.entities.models.ParkingLot;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.devices.Robot;

/** Implementation of a car insertion/retrieval algorithm.
 * The algorithm is used to pack cars into the internal storage (parking cell array) of a parking lot. */
@SuppressWarnings("unused")
public class CarTransportationControllerA extends RequestController implements CarTransportationController {

  public CarTransportationControllerA(ServerController serverController) {
    super(serverController);
  }

  /** The robots. */
  private Map<String, Robot> robots;

  /** Insert multiple cars. This function should only be run after checking there
   * is at least one empty space in the parking lot.
   * @param conn the SQL connection
   * @param lot the parking lot
   * @param carIds Stack holding the car IDs
   * @param exitTimes Stack holding the exit times, correlated with car IDs
   * @return true, if successful
   * @throws SQLException
   *         on error
   * @throws ServerException
   *         if an error occurs during insertion */
  private boolean insertCars(Connection conn, ParkingLot lot, Stack<String> carIds, Stack<LocalDateTime> exitTimes) throws SQLException, ServerException {
    // Get the parking lot
    ParkingCell[][][] content = lot.constructContentArray(conn);
    int lotWidth = lot.getWidth();

    /* Lower number represents higher priority - the car will be closer to exit
     * 0 <--> size+3 (7,11,15) We have a priority for each Row and another three
     * for the time when all the lot is full except the front lines of three
     * Then we will start to fill them one by one */
    int priority, worstPriority = lotWidth + 3 - 1;
    int iSize, iHeight, maxSize, maxHeight, maxDepth, path, minPath;
    // Easy count of free spots in each priority
    int[] freeSpotsCount = new int[worstPriority + 1];
    for (int i = 0; i < worstPriority; i++) {
      freeSpotsCount[i] = 0;
    }
    /* Count priorities from 3 to worst */
    for (iSize = 0; iSize < lotWidth; iSize++) {
      for (iHeight = 0; iHeight < 5; iHeight++) {
        int j = Math.floorMod(iHeight, 3);
        int k = Math.floorDiv(iHeight, 3) + 1;
        if (content[iSize][j][k].isFree()) {
          freeSpotsCount[iSize + 3]++;
        }
      }
    }
    /* count priorities 0,1,2 */
    for (iHeight = 0; iHeight < 3; iHeight++) {
      for (iSize = lotWidth - 1; iSize >= 0; iSize--) {
        if (content[iSize][iHeight][0].isFree()) {
          freeSpotsCount[iHeight]++;
        }
      }
    }

    /* This algorithm will find parking spots for the cars */

    while (!carIds.isEmpty()) {
      String carId = carIds.pop();
      LocalDateTime exitTime = exitTimes.pop();

      priority = worstPriority;
      /* Optimal coordinates for insertion */
      maxSize = -1;
      maxHeight = -1;
      maxDepth = -1;

      priority = calculatePriority(exitTime, worstPriority, lotWidth);

      // Find spot for the car, based on priority
      /* Seek a place among this and worse priorities */
      for (iSize = priority - 3; (iSize <= worstPriority - 3) && (maxSize == -1); iSize++) {
        if (freeSpotsCount[iSize + 3] != 0) {
          // If there is at least one free space here find it
          for (iHeight = 5; (iHeight >= 0) && (maxSize == -1); iHeight--) {
            int j = Math.floorMod(iHeight, 3);
            int k = Math.floorDiv(iHeight, 3) + 1;
            if (content[iSize][j][k].isFree()) {
              maxSize = iSize;
              maxHeight = Math.floorMod(iHeight, 3);
              maxDepth = Math.floorDiv(iHeight, 3) + 1;
            }
          }
        } else {
          // if there are no free spaces here, try to promote someone
          int otherPriority;
          // For every car in "priority block"
          for (iHeight = 5; (iHeight >= 0) && (maxSize == -1); iHeight--) {
            // Parse the car info
            ParkingCell cell = content[iSize][Math.floorMod(iHeight, 3)][Math.floorDiv(iHeight, 3) + 1];
            // Calculate the priority
            if (cell.getPlannedExitTime() == null) {
              continue;
            }

            otherPriority = calculatePriority(cell.getPlannedExitTime().toLocalDateTime(), worstPriority, lotWidth);
            /* If this one can be promoted, try to find a place for him in
             * higher priorities */
            if (otherPriority < iSize + 3 && otherPriority < priority) {
              for (int i = iSize - 1; (i >= otherPriority - 3) && (maxSize == -1); i--) {
                /* If there is a spot for him - mark his current spot as a spot
                 * for our insertion */
                if (freeSpotsCount[i + 3] != 0) {
                  maxSize = iSize;
                  maxHeight = Math.floorMod(iHeight, 3);
                  maxDepth = Math.floorDiv(iHeight, 3) + 1;
                }
              }
            } else {
              /* If our priority is higher (lower number) than other - try to
               * demote him. This is aggressive demotiong and should be deleted
               * if it turns out it's too aggressive. * This part have been
               * tested and it seems to be working properly */
              if (otherPriority > priority) {

                for (int i = iSize + 1; (i < lotWidth) && (maxSize == -1); i++) {
                  if (freeSpotsCount[i + 3] != 0) {
                    maxSize = iSize;
                    maxHeight = Math.floorMod(iHeight, 3);
                    maxDepth = Math.floorDiv(iHeight, 3) + 1;
                  }
                }
              }
            }
          }
        }
      }
      /* if no spot was found among worse priorities, start rising up */
      for (iSize = priority - 3 - 1; (iSize >= 0) && (maxSize == -1); iSize--) {
        if (freeSpotsCount[iSize + 3] != 0) {
          // If there is at least one free space here find it
          for (iHeight = 5; (iHeight >= 0) && (maxSize == -1); iHeight--) {
            int j = Math.floorMod(iHeight, 3);
            int k = Math.floorDiv(iHeight, 3) + 1;
            if (content[iSize][j][k].isFree()) {
              maxSize = iSize;
              maxHeight = Math.floorMod(iHeight, 3);
              maxDepth = Math.floorDiv(iHeight, 3) + 1;
            }
          }
        } else {
          // if there are no free spaces here, try to demote someone
          int otherPriority;
          // For every car in "priority block"
          for (iHeight = 5; (iHeight >= 0) && (maxSize == -1); iHeight--) {
            // Parse the car info
            ParkingCell cell = content[iSize][Math.floorMod(iHeight, 3)][Math.floorDiv(iHeight, 3) + 1];
            // Calculate the priority
            if (cell.getPlannedExitTime() == null) {
              continue;
            }
            otherPriority = calculatePriority(cell.getPlannedExitTime().toLocalDateTime(), worstPriority, lotWidth);
            /* If this one can be demoted, try to find a place for him in lower
             * priorities */
            if (otherPriority > iSize + 3 && otherPriority > priority) {
              // otherPriority - 3 is a row (in iSize) where this car should
              // have been standing
              for (int i = otherPriority - 3 + 1; (i < lotWidth) && (maxSize == -1); i++) {
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

      /* If all fails find a spot among emergency priorities */
      for (iHeight = 2; (iHeight >= 0) && (maxHeight == -1); iHeight--) {
        for (iSize = lotWidth - 1; ((iSize >= 0) && (maxSize == -1)); iSize--) {
          if (content[iSize][iHeight][0].isFree()) {
            maxSize = iSize;
            maxHeight = iHeight;
            maxDepth = 0;
          }
        }
      }
      /* By this time there should be a spot for us to insert a car Now we have
       * to pave the way to the spot because there may be other cars in the way */
      if (maxSize == -1) {
        System.out.printf("Could not find a spot for the car %s at %s\n", carId, exitTime);
      }

      pave(carIds, exitTimes, maxSize, maxHeight, maxDepth, content, freeSpotsCount);

      // insert the car
      System.out.println(String.format("Inserting car %s into location %s, %s, %s", carId, maxSize, maxHeight, maxDepth));
      ParkingCell cell = content[maxSize][maxHeight][maxDepth];
      cell.setCarID(carId);
      cell.setPlannedExitTime(Timestamp.valueOf(exitTime));
      // update priorities map
      if (maxDepth == 0) {
        freeSpotsCount[maxHeight]--;
      } else {
        freeSpotsCount[maxSize + 3]++;
      }
      // call a robot
      // this.robots.get(Integer.parseInt(lot.getRobotIP())).insertCar(carId,
      // maxSize,maxHeight, maxDepth);
    }

    lot.updateContent(conn, content);

    return true;

  }

  /** Attempt to insert the car into the lot. Optimal coordinates are calculated
   * before insertion If the lot is full, or some other error occurs,
   * LotController will return an appropriate error response, which we will send
   * back to the user.
   * @param conn
   *        the SQL connection
   * @param lot
   *        the parking lot
   * @param carId
   *        the car id
   * @param exitTime
   *        the exit time
   * @throws SQLException
   *         on error
   * @throws ServerException
   *         if an error occurs during insertion */
  public void insertCar(Connection conn, ParkingLot lot, String carId, LocalDateTime exitTime) throws SQLException, ServerException {
    Stack<String> carIds = new Stack<String>();
    carIds.push(carId);

    Stack<LocalDateTime> exitTimes = new Stack<LocalDateTime>();
    exitTimes.push(exitTime);

    // check if there is free space at all
    if (lot.countFreeCells(conn) <= 0) {
      throw new ServerException("No free space in the parking lot!");
    }

    if (!insertCars(conn, lot, carIds, exitTimes)) {
      throw new ServerException("Car Insertion failed");
    }

    // printLotContent(lot.constructContentArray(conn));
  }

  void printLotContent(ParkingCell[][][] content) {
    Gson gson = new Gson();

    for (ParkingCell[][] i : content) {
      for (ParkingCell[] j : i) {
        for (ParkingCell cell : j) {
          System.out.println(gson.toJson(cell));
        }
      }
    }
  }

  /** This function will calculate how many cars we need to move in order to
   * insert a car into the specified cell.
   * @param content
   *        Lot content as array
   * @param iSize
   *        X coordinate of the spot
   * @param iHeight
   *        Y coordinate of the spot
   * @param iDepth
   *        Z coordinate of the spot
   * @return Number of cars that are in the way of the desired car insertion */
  private int calculatePath(String[][][] content, int iSize, int iHeight, int iDepth) {
    // System.out.println(String.format("calculatePath(content, %s, %s, %s)",
    // iSize,
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

  /** Attempt to retrieve a car with the specified ID from the parking lot.
   * @param conn the SQL connection
   * @param lotId the parking lot ID
   * @param carID the car ID
   * @throws SQLException on error
   * @throws ServerException on error */
  public void retrieveCar(Connection conn, int lotId, String carID) throws SQLException, ServerException {
    ParkingLot lot = ParkingLot.findByID(conn, lotId);
    // String[][][] content = lot.getContentAsArray();
    ParkingCell[][][] content = lot.constructContentArray(conn);
    // printLotContent(content);

    // Get the robot in the parking lot
    /* Robot robbie = robots.get(lot.getRobotIP()); if (robbie == null) { return
     * false; } */

    int eWidth = -1, eHeight = -1, eDepth = -1;
    boolean found = false;

    // Find a car in the lot by carId
    for (int iWidth = 0; iWidth < lot.getWidth() && !found; iWidth++) {
      for (int iHeight = 0; iHeight < 3 && !found; iHeight++) {
        for (int iDepth = 0; iDepth < 3 && !found; iDepth++) {
          if (content[iWidth][iHeight][iDepth].contains(carID)) {
            // When found, mark the spot as empty and remember the location
            eWidth = iWidth;
            eHeight = iHeight;
            eDepth = iDepth;

            content[iWidth][iHeight][iDepth].clear();

            found = true;
          }
        }
      }
    }

    System.out.println(String.format("Retrieving car %s from location %s, %s, %s", carID, eWidth, eHeight, eDepth));

    /* We need to remove all the cars in the way before we will be able to
     * "retrieve the car" */

    Stack<String> carIds = new Stack<String>();
    Stack<LocalDateTime> exitTimes = new Stack<LocalDateTime>();
    for (int iWidth = 0; iWidth < eWidth; iWidth++) {
      ParkingCell cell = content[iWidth][0][0];
      if (cell.containsCar()) {
        carIds.push(cell.getCarID());
        exitTimes.push(cell.getPlannedExitTime().toLocalDateTime());
        content[iWidth][0][0].clear();
        // robbie.retrieveCar(carIds.peek(), eSize, iHeight, 0);
      }
    }
    for (int iHeight = 0; iHeight < eHeight; iHeight++) {
      ParkingCell cell = content[eWidth][iHeight][0];
      if (cell.containsCar()) {
        carIds.push(cell.getCarID());
        exitTimes.push(cell.getPlannedExitTime().toLocalDateTime());
        content[eWidth][iHeight][0].clear();
        // robbie.retrieveCar(carIds.peek(), eSize, iHeight, 0);
      }
    }

    for (int iDepth = 0; iDepth < eDepth; iDepth++) {
      ParkingCell cell = content[eWidth][eHeight][iDepth];
      if (cell.containsCar()) {
        carIds.push(cell.getCarID());
        exitTimes.push(cell.getPlannedExitTime().toLocalDateTime());
        content[eWidth][eHeight][iDepth].clear();
        // robbie.retrieveCar(carIds.peek(), eSize, iHeight, iDepth);
      }
    }

    // Update lot content
    lot.updateContent(conn, content);

    // robbie.retrieveCar(carID, eSize, iHeight, iDepth);
    if (!insertCars(conn, lot, carIds, exitTimes)) {
      throw new ServerException("Failed to retrieve car");
    }
  }

  private int calculatePriority(LocalDateTime exitTime, int worstPriority, int lotSize) {
    int priority = worstPriority;
    LocalDateTime fullSubscriptionTime = LocalDateTime.of(now().toLocalDate(), LocalTime.MIDNIGHT);
    /* Calculate exit priority if there is no exit time - it means we deal with
     * FULL SUBSCRIPTION which will perhaps stay for long hours or even more
     * than one day */
    if (exitTime == null || exitTime.equals(fullSubscriptionTime)) {
      // assign worst priority
      priority = worstPriority;
    } else {
      // if more than two days - worst priority
      if (exitTime.isAfter(now().plusDays(2))) {
        priority = worstPriority;
      } else {
        /* if less that two days divide minutes until exit by minutes in two
         * days it will give us a number between 0 and 1 multiply by size and
         * add 3 this means that we will attempt to inserts the car further back
         * before trying to put it somewhere in the way of transportation */
        priority = (int) (3 + (lotSize) * (now().until(exitTime, ChronoUnit.MINUTES)) / (LocalTime.MAX.toSecondOfDay() * 2 / 60));
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

  private void pave(Stack<String> carIds, Stack<LocalDateTime> exitTimes, int maxSize, int maxHeight, int maxDepth, ParkingCell[][][] content,
      int[] freeSpotsCount) {
    for (int i = 0; i < maxSize; i++) {
      if (content[i][0][0].containsCar()) {
        ParkingCell cell = content[i][0][0];
        carIds.push(cell.getCarID());
        exitTimes.push(cell.getPlannedExitTime().toLocalDateTime());
        cell.clear();
        freeSpotsCount[0]++;
      }
    }

    for (int i = 0; i < maxHeight; i++) {
      if (content[maxSize][i][0].containsCar()) {
        ParkingCell cell = content[maxSize][i][0];
        carIds.push(cell.getCarID());
        exitTimes.push(cell.getPlannedExitTime().toLocalDateTime());
        cell.clear();
        freeSpotsCount[i]++;
      }
    }

    for (int i = 0; i < maxDepth; i++) {
      if (content[maxSize][maxHeight][i].containsCar()) {
        ParkingCell cell = content[maxSize][maxHeight][i];
        carIds.push(cell.getCarID());
        exitTimes.push(cell.getPlannedExitTime().toLocalDateTime());
        cell.clear();

        if (i == 0) {
          freeSpotsCount[maxHeight]++;
        } else {
          freeSpotsCount[maxSize + 3]++;
        }
      }
    }

    if (content[maxSize][maxHeight][maxDepth].containsCar()) {
      ParkingCell cell = content[maxSize][maxHeight][maxDepth];
      carIds.push(cell.getCarID());
      exitTimes.push(cell.getPlannedExitTime().toLocalDateTime());
      cell.clear();

      if (maxDepth == 0) {
        freeSpotsCount[maxHeight]++;
      } else {
        freeSpotsCount[maxDepth + 3]++;
      }
    }
  }

}
