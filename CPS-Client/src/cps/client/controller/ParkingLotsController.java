/**
 * 
 */
package cps.client.controller;

import java.util.List;

import cps.entities.models.ParkingLot;

/**
 * Created on: 2018-01-14 1:39:14 AM 
 */
public interface ParkingLotsController extends ViewController {
  /**
   * @param parkingLots
   */
  public void setParkingLots(List<ParkingLot> list);
}
