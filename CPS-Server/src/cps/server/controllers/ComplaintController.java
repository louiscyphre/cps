/*
 * 
 */
package cps.server.controllers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

import cps.api.action.ListComplaintsAction;
import cps.api.action.RefundAction;
import cps.api.action.RejectComplaintAction;
import cps.api.request.ComplaintRequest;
import cps.api.request.ListMyComplaintsRequest;
import cps.api.response.ComplaintResponse;
import cps.api.response.ListComplaintsResponse;
import cps.api.response.ListMyComplaintsResponse;
import cps.api.response.RefundResponse;
import cps.api.response.RejectComplaintResponse;
import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.entities.models.Complaint;
import cps.entities.models.Customer;
import cps.entities.models.MonthlyReport;
import cps.entities.models.ParkingLot;
import cps.entities.people.User;
import cps.server.ServerController;
import cps.server.session.CustomerSession;
import cps.server.session.ServiceSession;
import cps.server.session.UserSession;
import cps.server.statistics.StatisticsCollector;

/** Handles requests that deal with customer complaints. */
public class ComplaintController extends RequestController {

  public ComplaintController(ServerController serverController) {
    super(serverController);
  }

  /** Called when the user wants to file a complaint.
   * @param request the request
   * @param session the session
   * @return the server response */
  public ServerResponse handle(ComplaintRequest request, UserSession session) {
    return database.performQuery(new ComplaintResponse(), (conn, response) -> {
      // Check if user is logged in
      User customer = session.requireUser();
      
      // Check if lot exists
      ParkingLot lot = ParkingLot.findByID(conn, request.getLotID());
      errorIfNull(lot, String.format("Parking Lot with id %d does not exist", request.getLotID()));
      
      Complaint complaint = Complaint.create(conn, customer.getId(), request.getLotID(), request.getContent(),
          Timestamp.valueOf(LocalDateTime.now()), null);

      errorIfNull(complaint, "Failed to create complaint");

      response.setComplaintID(complaint.getId());
      response.setSuccess("Complaint created successfully");
      
      // XXX Statistics
      // Add complaint to monthly statistics
      StatisticsCollector.increaseComplaints(conn, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), request.getLotID());

      return response;
    });
  }

  /** Called when an employee wants to refund a customer who submitted a complaint.
   * @param action the action
   * @param session the session
   * @return the server response */
  public ServerResponse handle(RefundAction action, UserSession session) {
    return database.performQuery(new RefundResponse(), (conn, response) -> {
      User employee = session.requireUser();

      errorIf(!employee.canAccessDomain(Constants.ACCESS_DOMAIN_CUSTOMER_SERVICE), "You cannot perform this action");
      errorIf(employee.getAccessLevel() < Constants.ACCESS_LEVEL_CUSTOMER_SERVICE_WORKER, "You cannot perform this action");

      Complaint complaint = Complaint.findByIDNotNull(conn, action.getComplaintID());

      Customer customer = Customer.findByIDNotNull(conn, complaint.getCustomerID());
      customer.receiveRefund(conn, action.getAmount());

      complaint.setEmployeeID(employee.getId());
      complaint.setRefundAmount(action.getAmount());
      complaint.setReason(action.getReason());
      complaint.setResolvedAt(Timestamp.valueOf(LocalDateTime.now()));
      complaint.setStatus(Constants.COMPLAINT_STATUS_ACCEPTED);
      complaint.update(conn);

      //XXX Statistics
      // Count as closed and refunded
      MonthlyReport.increaseClosed(conn, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), complaint.getLotID());
      MonthlyReport.increaseRefunded(conn, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), complaint.getLotID());      

      response.setComplaintID(complaint.getId());
      response.setCustomerID(customer.getId());
      response.setAmount(action.getAmount());
      response.setSuccess("Refund completed successfully");
      return response;
    });
  }

  /** Called when a complaint was deemed invalid, and the customer service employee decides to close it.
   * @param action the action
   * @param session the session
   * @return the server response */
  public ServerResponse handle(RejectComplaintAction action, ServiceSession session) {
    return database.performQuery(new RejectComplaintResponse(), (conn, response) -> {
      User employee = session.requireUser();

      errorIf(!employee.canAccessDomain(Constants.ACCESS_DOMAIN_CUSTOMER_SERVICE), "You cannot perform this action");
      errorIf(employee.getAccessLevel() < Constants.ACCESS_LEVEL_CUSTOMER_SERVICE_WORKER,
          "You cannot perform this action");

      Complaint complaint = Complaint.findByIDNotNull(conn, action.getComplaintID());

      Customer customer = Customer.findByIDNotNull(conn, complaint.getCustomerID());

      complaint.setEmployeeID(employee.getId());
      complaint.setReason(action.getReason());
      complaint.setResolvedAt(Timestamp.valueOf(LocalDateTime.now()));
      complaint.setStatus(Constants.COMPLAINT_STATUS_REJECTED);
      complaint.update(conn);
      
      //XXX Statistics
      //Count as closed
      MonthlyReport.increaseClosed(conn, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), complaint.getLotID());

      response.setComplaintID(complaint.getId());
      response.setCustomerID(customer.getId());
      response.setReason(action.getReason());
      response.setSuccess("Complaint rejected successfully");
      return response;
    });
  }

  /** Is used to produce a list of all complaints for the currently logged in customer.
   * @param request the client request
   * @param session the customer session
   * @return a list of the customer's complaints */
  public ServerResponse handle(ListMyComplaintsRequest request, CustomerSession session) {
    return database.performQuery(new ListMyComplaintsResponse(), (conn, response) -> {
      Customer customer = session.requireCustomer(); // Customer must be logged in
      Collection<Complaint> result = Complaint.findByCustomerID(conn, customer.getId());

      // This shouldn't happen - at least an empty list should always be returned
      errorIfNull(result, "Failed to create list of Complaint entries");

      response.setData(result);
      response.setCustomerID(request.getCustomerID());
      response.setSuccess("List of complaints retrieved successfully");
      return response;
    });
  }

  /** Is used to produce a list of all complaints in the system, for customer service employees to be able to view and navigate complaints.
   * @param action the action
   * @param session the session
   * @return a list of all complaints in the system */
  public ServerResponse handle(ListComplaintsAction action, ServiceSession session) {
    return database.performQuery(new ListComplaintsResponse(), (conn, response) -> {
      session.requireUser(); // Employee must be logged in

      Collection<Complaint> result = Complaint.findAll(conn);

      // This shouldn't happen - at least an empty list should always be returned
      errorIfNull(result, "Failed to create list of Complaint entries");

      response.setData(result);
      response.setSuccess("List of complaints retrieved successfully");
      return response;
    });
  }

}
