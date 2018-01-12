package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import cps.api.request.ParkingEntryRequest;
import cps.api.response.ParkingEntryResponse;
import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.entities.models.CarTransportation;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.entities.models.ParkingService;
import cps.entities.models.SubscriptionService;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.session.CustomerSession;

/**
 * The Class EntryExitController.
 */
public class ParkingEntryController extends RequestController {

  /**
   * Instantiates a new entry exit controller.
   *
   * @param serverController
   *          the server application
   */
  public ParkingEntryController(ServerController serverController) {
    super(serverController);
  }

  /**
   * Handle ParkingEntryRequest.
   *
   * @param request
   *          the request
   * @param session
   * @return the server response
   */
  public ServerResponse handle(ParkingEntryRequest request, CustomerSession session) {
    return database.performQuery(new ParkingEntryResponse(), (conn, response) -> {
      // Shortcuts for commonly used properties
      int customerID = request.getCustomerID();
      String carID = request.getCarID();
      int lotID = request.getLotID();

      // Check that an entry license exists
      ParkingService service = findEntryLicense(conn, response, request);

      // Entry license exists - continue
      // Find the parking lot that the customer wants to enter
      ParkingLot lot = ParkingLot.findByIDNotNull(conn, lotID);

      // Attempt to insert the car into the lot.
      // Optimal coordinates are calculated before insertion.
      // If the lot is full, or some other error occurs, LotController will
      // return an appropriate error response, which we will send back to the
      // user.
      // The function will create table entry to record where the car was
      // placed
      CarTransportationController transportationController = serverController.getTransportationController();
      transportationController.insertCar(conn, lot, carID, service.getExitTime());

      // All good - create a CarTransportation table entry to record the fact
      // that a successful parking was made.
      // Throws exception on failure, which will be automatically handled up in
      // the control chain.
      CarTransportation.create(conn, request.getCustomerID(), request.getCarID(), service.getLicenseType(),
          service.getId(), request.getLotID());

      response.setLotID(lotID);
      response.setCustomerID(customerID);
      response.setSuccess("ParkingEntry request completed successfully");
      return response;
    });
  }

  private ParkingService findEntryLicense(Connection conn, ServerResponse response, ParkingEntryRequest request)
      throws SQLException, ServerException {
    int customerID = request.getCustomerID();
    String carID = request.getCarID();
    int lotID = request.getLotID();

    if (request.getSubscriptionID() == 0) {
      // The customer wants to enter based on car ID only
      // Find the OnetimeService that gives this customer an entry license.
      // If the OnetimeService with the right parameters exists, then they can
      // park their car in specified parking lot.

      // Check that an entry license exists
      OnetimeService service = OnetimeService.findForEntry(conn, customerID, carID, lotID);

      errorIfNull(service,
          "OnetimeService entry license not found for customer ID " + customerID + " with car ID " + carID);

      return service;
    } else {
      // The customer wants to enter based on car ID and subscription ID
      // Check if they have a SubscriptionService entry
      int subsID = request.getSubscriptionID();
      SubscriptionService service = SubscriptionService.findForEntry(conn, customerID, carID, subsID);

      // Check that entry an license exists
      errorIfNull(service,
          "SubscriptionService entry license not found for customer ID " + customerID + " with car ID " + carID);

      // Check that the lot ID is correct
      errorIf(service.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_REGULAR && lotID != service.getLotID(),
          String.format("Requested parking in lotID = %s, but subscription is for lotID = %s", lotID,
              service.getLotID()));

      return service;
    }
  }
}
