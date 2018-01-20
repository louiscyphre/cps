/**
 * 
 */
package cps.client.controller;

import java.util.Collection;

import cps.entities.models.ParkingLot;

/**
 * Interface for controllers with ParkingLots
 */
public interface ParkingLotsController extends ViewController {
  /**
   * Sets the parking lots with accordance to given collection
   * @param parkingLots collection of parking lots
   * @see ParkingLot
   */
  public void setParkingLots(Collection<ParkingLot> parkingLots);
}
