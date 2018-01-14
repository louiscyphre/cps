package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import cps.api.request.ParkingExitRequest;
import cps.api.response.ParkingExitResponse;
import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.entities.models.CarTransportation;
import cps.entities.models.Customer;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.entities.models.SubscriptionService;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.session.CustomerSession;

/**
 * The Class EntryExitController.
 */
public class ParkingExitController extends RequestController {

  /**
   * Instantiates a new entry exit controller.
   *
   * @param serverController
   *          the server application
   */
  public ParkingExitController(ServerController serverController) {
    super(serverController);
  }

  /**
   * Handle ParkingExitRequest.
   *
   * @param request
   *          the request
   * @param session
   * @return the server response
   */
  public ServerResponse handle(ParkingExitRequest request, CustomerSession session) {
    return database.performQuery(new ParkingExitResponse(), (conn, response) -> {
      CarTransportation transportation = CarTransportation.findForExit(conn, request.getCustomerID(), request.getCarID(),
          request.getLotID());

      errorIfNull(transportation, "CarTransportation with the requested parameters does not exist");

      // Update the removedAt field
      Timestamp removedAt = new Timestamp(System.currentTimeMillis());
      transportation.updateRemovedAt(conn, removedAt); // there will be an SQL exception if this fails

      // Calculate the amount of money that the customer has to pay
      float sum = calculatePayment(conn, transportation, request);

      // Find customer
      Customer customer = Customer.findByIDNotNull(conn, request.getCustomerID());

      // Write payment
      customer.pay(conn, sum);

      /*
       * Attempt to retrieve the car from the lot The function will shuffle
       * the cars that get in the way in an attempt to locate them in
       * accordance to the current situation
       */
      CarTransportationController transportationController = serverController.getTransportationController();
      transportationController.retrieveCar(conn, request.getLotID(), request.getCarID());

      // Success
      response.setCustomerID(customer.getId());
      response.setPayment(sum);
      response.setSuccess("ParkingExit request completed successfully");

      return response;
    });
  }

  public static float calculatePayment(Connection conn, CarTransportation carTransportation,
      ParkingExitRequest exitRequest) throws SQLException, ServerException {

    // Determine if this is a subscription or one time
    switch (carTransportation.getAuthType()) {
      case Constants.LICENSE_TYPE_ONETIME:
        // If customer did not park by subscription charge according to parking
        // type
        return calculatePayment(conn, carTransportation, exitRequest,
            OnetimeService.findByIDNotNull(conn, carTransportation.getAuthID()));
      case Constants.LICENSE_TYPE_SUBSCRIPTION:
        // Calculate extra payment for staying overdue with a subscription
        return calculatePayment(conn, carTransportation, exitRequest,
            SubscriptionService.findByIDNotNull(conn, carTransportation.getAuthID()));
      default:
        throw new ServerException("Unknown service type");
    }

  }

  public static float calculatePayment(Connection conn, CarTransportation carTransportation,
      ParkingExitRequest exitRequest, OnetimeService service) throws SQLException, ServerException {
    // Determine prices at that parking lot
    ParkingLot parkingLot = ParkingLot.findByIDNotNull(conn, exitRequest.getLotID());
    float tariffLow = parkingLot.getPriceForService(service.getParkingType()) / 60;
    float tariffHigh = parkingLot.getPrice1() / 60; // fine price is higher

    // Was the customer late or early? negative means early positive means late
    long startedLate = carTransportation.getInsertedAt().getTime() - service.getPlannedStartTime().getTime();
    startedLate = startedLate / (60 * 1000); // convert to minutes

    // How much time the customer ordered?
    long inside = service.getPlannedEndTime().getTime() - service.getPlannedStartTime().getTime();
    inside = inside / (60 * 1000);

    // Has the customer left late or early? negative means early positive means
    // late
    long endedLate = carTransportation.getRemovedAt().getTime() - service.getPlannedEndTime().getTime();
    endedLate = endedLate / (60 * 1000);

    // If the customer running late less than 30 minutes - discard being late
    // and
    // charge full ordered time
    if (startedLate < 30) {
      startedLate = 0;
    } else {
      // if the customer came in early simply add minutes to total time and
      // charge
      // normal
      if (startedLate < 0) {
        inside -= startedLate;
      }
    }

    // If the customer exits early - charge full time and discard early minutes
    if (endedLate < 0) {
      endedLate = 0;
    }

    // At last we calculate the sum
    // The formula is according to the requirements
    float sum = startedLate * tariffLow * 1.2f + inside * tariffLow + endedLate * tariffHigh;
    
    if (service.getParkingType() == Constants.PARKING_TYPE_RESERVED) {
      // If this was a Reserved Parking, subtract the amount that the user had to pay in advance
      sum -= service.calculatePayment(parkingLot.getPriceForService(service.getParkingType()));
    }
    
    return sum;
  }

  public static float calculatePayment(Connection conn, CarTransportation carTransportation,
      ParkingExitRequest exitRequest, SubscriptionService service) throws SQLException, ServerException {
    if (service.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_REGULAR) {
      Timestamp plannedExitTime = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), service.getDailyExitTime()));

      long removedAt = carTransportation.getRemovedAt().getTime();
      long lateMinutes = (removedAt - plannedExitTime.getTime()) / 60 / 1000;

      if (lateMinutes > 0) {
        ParkingLot parkingLot = ParkingLot.findByIDNotNull(conn, exitRequest.getLotID());
        return lateMinutes * parkingLot.getPrice1() / 60;
      }
    } else if (service.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_FULL) {
      // Charge a fine if customer stays parked continuously for longer than 14
      // days
      // with a full subscription
      long maxMinutes = 14 * 24 * 60; // 14 days
      long removedAt = carTransportation.getRemovedAt().getTime();
      long insertedAt = carTransportation.getInsertedAt().getTime();
      long lateMinutes = (removedAt - insertedAt) / 60 / 1000 - maxMinutes;

      if (lateMinutes > 0) { // customer stayed parked longer than 14 days
        ParkingLot parkingLot = ParkingLot.findByIDNotNull(conn, exitRequest.getLotID());
        return lateMinutes * parkingLot.getPrice1() / 60;
      }
    } else {
      throw new ServerException("Unknown subscription type");
    }

    return 0f;
  }
}
