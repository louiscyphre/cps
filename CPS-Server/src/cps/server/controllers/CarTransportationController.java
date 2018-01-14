package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import cps.entities.models.ParkingLot;
import cps.server.ServerException;

public interface CarTransportationController {
  public void insertCar(Connection conn, ParkingLot lot, String carId, LocalDateTime exitTime) throws SQLException, ServerException;
  public void retrieveCar(Connection conn, int lotId, String carID) throws SQLException, ServerException;
}
