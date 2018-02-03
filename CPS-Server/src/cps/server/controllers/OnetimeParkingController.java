/*
 * 
 */
package cps.server.controllers;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import cps.api.request.CancelOnetimeParkingRequest;
import cps.api.request.IncidentalParkingRequest;
import cps.api.request.ListOnetimeEntriesRequest;
import cps.api.request.OnetimeParkingRequest;
import cps.api.request.ReservedParkingRequest;
import cps.api.response.CancelOnetimeParkingResponse;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.ListOnetimeEntriesResponse;
import cps.api.response.OnetimeParkingResponse;
import cps.api.response.ReservedParkingResponse;
import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.entities.models.Customer;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.session.CustomerSession;
import cps.server.session.UserSession;
import cps.server.statistics.StatisticsCollector;

/** Handles requests that deal with One-time Parking Services:
 * - Incidental Parking (Park now)
 * - Reserved Parking (Order now and park later) */
public class OnetimeParkingController extends RequestController {

  
  /** Instantiates a new onetime parking controller.
   * @param serverController the server application */
  public OnetimeParkingController(ServerController serverController) {
    super(serverController);
  }

  /** Generalized handler for both types of Onetime Parking requests.
   * 
   * Checks input parameters, updates database records, calculates payment, if applicable (Reserved Parking),
   * and charges the customer's account (if a payment was required).
   * 
   * If the user was not logged in while making this request, will create a new user and send them the password.
   * After this, the user will be able to log in with their email address and the generated password.
   * @param request the request
   * @param session the session
   * @param serverResponse the server response
   * @param startTime the start time
   * @param plannedEndTime the planned end time
   * @param now the now
   * @return the server response */
  private ServerResponse handle(OnetimeParkingRequest request, CustomerSession session,
      OnetimeParkingResponse serverResponse, Timestamp startTime, Timestamp plannedEndTime, LocalDateTime now) {
    return database.performQuery(serverResponse, (conn, response) -> {
      // Check that the same car is not going to be parked in different
      // locations at the same time
      errorIf(OnetimeService.overlapExists(conn, request.getCarID(), startTime, plannedEndTime),
          "Parking spot is already reserved for this car in this timeframe");

      // Check that the starting time is not in the past
      errorIf(startTime.toLocalDateTime().isBefore(now), "The specified starting date is in the past");

      // Check that parking end time is after the starting time
      long duration = (plannedEndTime.getTime() - startTime.getTime()) / 60 / 1000;
      errorIf(duration < 1, "Length of parking must be at least one minute");

      // Check that lot exists
      ParkingLot lot = ParkingLot.findByIDNotNull(conn, request.getLotID());

      // Check that lot is not full
      // session.requireLotNotFull(conn, gson, lot, response);
      int availableCells = lot.countFreeCells(conn)
          - ParkingLot.countOrderedCells(conn, lot.getId(), startTime, plannedEndTime);
      errorIf(lot.isLotFull() || availableCells <= 0,
          "The specified lot is full; please try one of the alternative lots");

      // Handle login
      Customer customer = session.requireRegisteredCustomer(conn, request.getCustomerID(), request.getEmail());

      // If the customer requested an Incidental Parking, they want to park
      // right now
      boolean setParked = request.getParkingType() == Constants.PARKING_TYPE_INCIDENTAL;

      // Create the service
      OnetimeService service = OnetimeService.create(conn, request.getParkingType(), customer.getId(),
          request.getEmail(), request.getCarID(), request.getLotID(), startTime, plannedEndTime, setParked, false,
          false, false);
      errorIfNull(service, "Failed to create OnetimeService entry");

      // Calculate payment for service
      float price = lot.getPriceForService(service.getParkingType());
      float payment = service.calculatePayment(price);

      // Notify customer of what the payment is going to be
      response.setPayment(payment);

      switch (service.getParkingType()) {
        case Constants.PARKING_TYPE_RESERVED:
          // If this was a reserved parking, customer has to pay in advance
          customer.pay(conn, payment);
          break;
        case Constants.PARKING_TYPE_INCIDENTAL:
          // If this was an incidental parking, customer's will be parked
          // automatically
          ParkingEntryController entryController = serverController.getEntryController();
          entryController.registerEntry(conn, lot, customer.getId(), request.getCarID(), service);
          break;
        default:
          error("Internal error: unknown parking type");
      }

      // XXX Statistics
      StatisticsCollector.increaseOnetime(conn, service.getId(), service.getLicenseType(), service.getParkingType(), lot.getId(), service.isWarned());

      // success
      response.setCustomerData(customer);
      response.setServiceID(service.getId());
      response.setSuccess("OnetimeParkingRequest completed successfully");
      return response;
    });
  }

  /** Called when the user wants to Park Now (Incidental Parking request).
   * Calls the generalized handler with the appropriate parameters for an Incidental Parking request.
   * If the user made an IncidentalParking request, their car will be parked immediately after that.
   * @param request the request
   * @param session the session
   * @return the server response */
  public ServerResponse handle(IncidentalParkingRequest request, CustomerSession session) {
    Timestamp startTime = new Timestamp(System.currentTimeMillis());
    Timestamp plannedEndTime = Timestamp.valueOf(request.getPlannedEndTime());
    IncidentalParkingResponse response = new IncidentalParkingResponse();
    return handle(request, session, response, startTime, plannedEndTime, startTime.toLocalDateTime());
  }

  /** Called when the user wants to make a Reserved Parking request.
   * Calls the generalized handler with the appropriate parameters for a Reserved Parking request.
   * @param request the request
   * @param session the session
   * @return the server response */
  public ServerResponse handle(ReservedParkingRequest request, CustomerSession session) {
    Timestamp startTime = Timestamp.valueOf(request.getPlannedStartTime());
    Timestamp plannedEndTime = Timestamp.valueOf(request.getPlannedEndTime());
    ReservedParkingResponse response = new ReservedParkingResponse();
    ServerResponse toRet = handle(request, session, response, startTime, plannedEndTime, now());
    if (toRet.success()) {
      // TODO Tegra - increase daily statistics for ReservedParkingRequest
    }

    return toRet;
  }

  /** Called when the user wants to cancel a parking reservation that they made with a ReservedParkingRequest.
   * @param request the request
   * @param session the session
   * @return the server response */
  public ServerResponse handle(CancelOnetimeParkingRequest request, UserSession session) {
    ServerResponse toRet = database.performQuery(new CancelOnetimeParkingResponse(), (conn, response) -> {
      // Mark Order as canceled
      OnetimeService service = OnetimeService.findByID(conn, request.getOnetimeServiceID());

      errorIfNull(service, String.format("OnetimeService with id %s not found", request.getOnetimeServiceID()));
      errorIf(service.isCompleted(), "This service was already completed");
      errorIf(service.isCanceled(), "This service was already canceled");
      errorIf(service.isParked(), "This service is currently being used for parking");

      // Calculate refund amount based on how late the cancel request was
      // submitted
      // relative to the parking start time
      Duration duration = Duration.between(now(), service.getPlannedStartTime().toLocalDateTime());

      float refundValue = 1.0f;

      if (duration.compareTo(Duration.ofHours(3)) >= 0) {
        refundValue = 0.9f;
      } else if (duration.compareTo(Duration.ofHours(1)) >= 0) {
        refundValue = 0.5f;
      } else {
        // response.setError("The service order cannot be canceled at this
        // time.");
        // return response;
        refundValue = 0f;
      }

      float refundAmount = 0f;

      try {
        ParkingLot lot = service.getParkingLot(conn);
        float pricePerHour = lot.getPriceForService(service.getParkingType());
        float servicePrice = service.calculatePayment(pricePerHour);
        refundAmount = servicePrice * refundValue;

        // Cancel the order
        service.setCanceled(true);
        service.update(conn); // sync with database

        // XXX Statistics
        // Increase daily counter of canceled orders
        StatisticsCollector.increaseCanceledOrder(conn, LocalDate.now(), service.getLotID());

        // Refund customer
        Customer customer = service.getCustomer(conn);
        customer.receiveRefund(conn, refundAmount);
      } catch (ServerException ex) {
        response.setError(ex.getMessage());
        return response;
      }

      // Return Server Response
      response.setCustomerID(request.getCustomerID());
      response.setOnetimeServiceID(request.getOnetimeServiceID());
      response.setRefundAmount(refundAmount);
      response.setSuccess("OnetimeService order canceled successfully");
      return response;
    });
    return toRet;
  }

  /** Returns a list of all one-time parking services that this customer had ordered.
   * @param request the request
   * @param session the session
   * @return the server response */
  public ServerResponse handle(ListOnetimeEntriesRequest request, UserSession session) {
    return database.performQuery(new ListOnetimeEntriesResponse(), (conn, response) -> {
      Collection<OnetimeService> result = OnetimeService.findByCustomerID(conn, request.getCustomerID());

      // This shouldn't happen - at least an empty list should always be
      // returned
      errorIfNull(result, "Failed to create list of OnetimeService entries");

      response.setData(result);
      response.setCustomerID(request.getCustomerID());
      response.setSuccess("List of OnetimeService entries retrieved successfully");
      return response;
    });
  }
}
