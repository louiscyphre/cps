/*
 * 
 */
package cps.server.controllers;

import java.sql.Timestamp;
import java.time.Duration;
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
import cps.entities.models.DailyStatistics;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.session.CustomerSession;
import cps.server.session.UserSession;

/**
 * The Class OnetimeParkingController.
 */
@SuppressWarnings("unused")
public class OnetimeParkingController extends RequestController {

  /**
   * Instantiates a new one-time parking controller.
   *
   * @param serverController
   *          the server application
   */
  public OnetimeParkingController(ServerController serverController) {
    super(serverController);
  }

  public ServerResponse handle(OnetimeParkingRequest request, CustomerSession session,
      OnetimeParkingResponse serverResponse, Timestamp startTime, Timestamp plannedEndTime) {
    return database.performQuery(serverResponse, (conn, response) -> {
      // Handle login
      Customer customer = session.requireRegisteredCustomer(conn, request.getCustomerID(), request.getEmail());

      // TODO check time overlap for this car with other parking services
      long duration = (plannedEndTime.getTime() - startTime.getTime()) / 60 / 1000;
      errorIf(duration < 1, "Length of parking must be at least one minute");
      
      // Check that lot exists
      ParkingLot lot = ParkingLot.findByIDNotNull(conn, request.getLotID());

      OnetimeService service = OnetimeService.create(conn, request.getParkingType(), customer.getId(),
          request.getEmail(), request.getCarID(), request.getLotID(), startTime, plannedEndTime, false);
      errorIfNull(service, "Failed to create OnetimeService entry");
      
      // Calculate payment for service
      float price = lot.getPriceForService(service.getParkingType());
      float payment = service.calculatePayment(price);
      
      // Notify customer of what the payment is going to be
      response.setPayment(payment);
      
      if (service.getParkingType() == Constants.PARKING_TYPE_RESERVED) {        
        // If this was a reserved parking, customer has to pay in advance
        customer.pay(conn, payment);
      }

      // success
      response.setCustomerData(customer);
      response.setServiceID(service.getId());
      response.setSuccess("OnetimeParkingRequest completed successfully");
      return response;
    });
  }

  /**
   * Handle IncidentalParkingRequest.
   *
   * @param request
   *          the request
   * @param session
   * @return the server response
   */
  public ServerResponse handle(IncidentalParkingRequest request, CustomerSession session) {
    Timestamp startTime = new Timestamp(System.currentTimeMillis());
    Timestamp plannedEndTime = Timestamp.valueOf(request.getPlannedEndTime());
    IncidentalParkingResponse response = new IncidentalParkingResponse();
    return handle(request, session, response, startTime, plannedEndTime);
  }

  /**
   * Handle ReservedParkingRequest.
   *
   * @param request
   *          the request
   * @return the server response
   */
  public ServerResponse handle(ReservedParkingRequest request, CustomerSession session) {
    Timestamp startTime = Timestamp.valueOf(request.getPlannedStartTime());
    Timestamp plannedEndTime = Timestamp.valueOf(request.getPlannedEndTime());
    ReservedParkingResponse response = new ReservedParkingResponse();
    return handle(request, session, response, startTime, plannedEndTime);
  }

  /**
   * Handle CancelOnetimeParkingRequest.
   *
   * @param request
   *          the request
   * @param session
   * @return the server response
   */
  public ServerResponse handle(CancelOnetimeParkingRequest request, UserSession session) {
    return database.performQuery(conn -> {
      CancelOnetimeParkingResponse response = new CancelOnetimeParkingResponse();
      // Mark Order as canceled
      OnetimeService service = OnetimeService.findByID(conn, request.getOnetimeServiceID());

      if (service == null) {
        response.setError(String.format("OnetimeService with id %s not found", request.getOnetimeServiceID()));
        return response;
      }

      // Calculate refund amount based on how late the cancel request was
      // submitted
      // relative to the parking start time
      Duration duration = Duration.between(LocalDateTime.now(), service.getPlannedStartTime().toLocalDateTime());

      float refundValue = 1.0f;

      if (duration.compareTo(Duration.ofHours(3)) >= 0) {
        refundValue = 0.9f;
      } else if (duration.compareTo(Duration.ofHours(1)) >= 0) {
        refundValue = 0.5f;
      } else {
        response.setError("The service order cannot be canceled at this time.");
        return response;
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

        // Increase daily counter of canceled orders
        DailyStatistics.increaseCanceledOrder(conn, service.getLotID());

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
  }

  /**
   * Handle List One Time Entries Request.
   *
   * @param request
   *          the request
   * @return the server response
   */
  public ServerResponse handle(ListOnetimeEntriesRequest request, UserSession session) {
    return database.performQuery(conn -> {
      Collection<OnetimeService> result = OnetimeService.findByCustomerID(conn, request.getCustomerID());
      return new ListOnetimeEntriesResponse(result, request.getCustomerID());
    });
  }
}
