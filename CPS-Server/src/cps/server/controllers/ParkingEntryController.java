package cps.server.controllers;

import static cps.common.Utilities.isWeekend;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import cps.api.request.ParkingEntryRequest;
import cps.api.response.ParkingEntryResponse;
import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.entities.models.CarTransportation;
import cps.entities.models.Customer;
import cps.entities.models.DailyStatistics;
import cps.entities.models.MonthlyReport;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.entities.models.ParkingService;
import cps.entities.models.SubscriptionService;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.session.CustomerSession;

/** The Class EntryExitController. */
public class ParkingEntryController extends RequestController {

  /** Instantiates a new entry exit controller.
   * @param serverController
   *        the server application */
  public ParkingEntryController(ServerController serverController) {
    super(serverController);
  }

  /** Handle ParkingEntryRequest.
   * @param request
   *        the request
   * @param session
   * @return the server response */
  public ServerResponse handle(ParkingEntryRequest request, CustomerSession session) {
    return database.performQuery(new ParkingEntryResponse(), (conn, response) -> {
      Customer customer = session.requireCustomer();
      // Shortcuts for commonly used properties
      int customerID = customer.getId();
      String carID = request.getCarID();
      int lotID = request.getLotID();

      // Check that an entry license exists
      ParkingService service = findEntryLicense(conn, response, request);

      // Entry license exists - continue
      // Find the parking lot that the customer wants to enter
      ParkingLot lot = ParkingLot.findByIDNotNull(conn, lotID);
      registerEntry(conn, lot, customer.getId(), carID, service);

      response.setLotID(lotID);
      response.setCustomerID(customerID);
      response.setSuccess("ParkingEntry request completed successfully");

      return response;
    });
  }

  public void registerEntry(Connection conn, ParkingLot lot, int customerID, String carID, ParkingService service)
      throws SQLException, ServerException {
    // Check that this car is allowed to enter
    // CarTransportation transportation = CarTransportation.findForEntry(conn,
    // customerID, carID, lot.getId());
    // errorIf(transportation != null, "The car with the specified ID was
    // already parked");

    // Check that the lot does not already contain the car
    errorIf(lot.contains(lot.constructContentArray(conn), carID), "This car is already parked in the chosen lot");

    // Attempt to insert the car into the lot.
    // Optimal coordinates are calculated before insertion.
    // If the lot is full, or some other error occurs, LotController will
    // return an appropriate error response, which we will send back to the
    // user.
    // The function will create table entry to record where the car was
    // placed
    CarTransportationController transportationController = serverController.getTransportationController();
    transportationController.insertCar(conn, lot, carID, service.getExitTime());

    // XXX Statistics
    if (service.getLicenseType() == Constants.LICENSE_TYPE_ONETIME) {
      // Update daily statistics - realized orders
      DailyStatistics.increaseRealizedOrder(conn, lot.getId());
      if (((OnetimeService) service).getParkingType() == Constants.PARKING_TYPE_INCIDENTAL) {
        // Monthly statistics
        MonthlyReport.increaseIncidental(conn, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
            lot.getId());
      } else {
        // Daily statistics - late arrival
        if (((OnetimeService) service).isWarned()) {
          DailyStatistics.increaseLateArrival(conn, service.getId());
        }
        // Monthly statistics
        MonthlyReport.increaseReserved(conn, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
            lot.getId());
      }
    }

    if (!service.isParked()) {
      service.setParked(true);
      service.update(conn);
    }

    // All good - create a CarTransportation table entry to record the fact
    // that a successful parking was made.
    // Throws exception on failure, which will be automatically handled up in
    // the control chain.
    CarTransportation.create(conn, customerID, carID, service.getLicenseType(), service.getId(), lot.getId());

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
      errorIf(service.isCompleted(), "This reservation was already completed");
      errorIf(service.isCanceled(), "This reservation was canceled");
      errorIf(service.getPlannedStartTime().toLocalDateTime().isAfter(LocalDateTime.now()),
          "Trying to enter before the starting time that was specified for this reservation");

      return service;
    } else {
      // The customer wants to enter based on car ID and subscription ID
      // Check if they have a SubscriptionService entry
      int subsID = request.getSubscriptionID();
      SubscriptionService service = SubscriptionService.findForEntry(conn, customerID, carID, subsID);
      // TODO is it possible to cancel a subscription?

      // FIXME allow entry only once a day for regular subscription

      // Check that entry an license exists
      errorIfNull(service,
          "SubscriptionService entry license not found for customer ID " + customerID + " with car ID " + carID);

      // Check that the lot ID is correct for regular subscription
      errorIf(service.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_REGULAR && lotID != service.getLotID(),
          String.format("Requested parking in lotID = %s, but subscription is for lotID = %s", lotID,
              service.getLotID()));

      // Check that the user is not trying to enter with a regular subscription
      // on a weekend
      DayOfWeek currentDay = LocalDate.now().getDayOfWeek();
      errorIf(service.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_REGULAR && isWeekend(currentDay),
          "Regular subscription cannot be used to park during weekends");

      return service;
    }
  }
}
