package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import cps.entities.models.ParkingLot;
import cps.server.ServerException;

/** Is used to make it possible to swap out different insertion/retrieval algorithms.
 * The insertion/retrieval algorithm is used to pack cars into the internal storage (parking cell array) of a parking lot. */
public interface CarTransportationController {
  
  /** Insert a car into the parking lot.
   * @param conn the SQL connection
   * @param lot the lot
   * @param carId the car id
   * @param exitTime the exit time
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public void insertCar(Connection conn, ParkingLot lot, String carId, LocalDateTime exitTime) throws SQLException, ServerException;
  
  
  /** Retrieve a car from the parking lot.
   * @param conn the SQL connection
   * @param lotId the lot id
   * @param carID the car ID
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public void retrieveCar(Connection conn, int lotId, String carID) throws SQLException, ServerException;
}
