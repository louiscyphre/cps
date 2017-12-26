/**
 * 
 */
package cps.server.controllers;

import java.sql.Timestamp;

import cps.api.request.IncidentalParkingRequest;
import cps.api.request.ParkingEntryRequest;
import cps.api.request.ParkingExitRequest;
import cps.api.response.ServerResponse;
import cps.common.Utilities.Holder;
import cps.entities.models.CarTransportation;
import cps.entities.models.OnetimeService;
import cps.server.ServerApplication;

/**
 * @author student
 *
 */
public class EntryExitController extends RequestController {

  public EntryExitController(ServerApplication serverApplication) {
    super(serverApplication);
    // TODO Auto-generated constructor stub
  }

  public ServerResponse handle(ParkingEntryRequest request) {
    Holder<CarTransportation> result = new Holder<>(null);
    databaseController.performAction(conn -> {
      // TODO: implement
    });

    // System.out.println(result.getValue());
    return ServerResponse.decide("Entry creation", result.getValue() != null);
  }

  public ServerResponse handle(ParkingExitRequest request) {
    Holder<CarTransportation> result = new Holder<>(null);
    databaseController.performAction(conn -> {
      // TODO: implement
    });

    // System.out.println(result.getValue());
    return ServerResponse.decide("Entry creation", result.getValue() != null);
  }

}
