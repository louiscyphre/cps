/**
 * 
 */
package cps.client.controller;

import java.util.Collection;

import cps.api.response.ListParkingLotsResponse;
import cps.api.response.ServerResponse;
import cps.entities.models.ParkingLot;

/**
 * Created on: 2018-01-14 1:39:14 AM
 */
public interface ParkingLotsController extends ViewController {
  /**
   * @param response
   */
  public ServerResponse handleParkingLots(ListParkingLotsResponse response);
}
