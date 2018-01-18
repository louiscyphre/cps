package cps.server.controllers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

import cps.api.action.ListComplaintsAction;
import cps.api.action.RefundAction;
import cps.api.request.ComplaintRequest;
import cps.api.request.ListMyComplaintsRequest;
import cps.api.response.ComplaintResponse;
import cps.api.response.ListComplaintsResponse;
import cps.api.response.ListMyComplaintsResponse;
import cps.api.response.RefundResponse;
import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.entities.models.Complaint;
import cps.entities.models.Customer;
import cps.entities.people.User;
import cps.server.ServerController;
import cps.server.session.CustomerSession;
import cps.server.session.ServiceSession;
import cps.server.session.UserSession;

public class ComplaintController extends RequestController {

  public ComplaintController(ServerController serverController) {
    super(serverController);
  }

  public ServerResponse handle(ComplaintRequest request, UserSession session) {
    return database.performQuery(new ComplaintResponse(), (conn, response) -> {
      Complaint complaint = Complaint.create(conn, request.getCustomerID(), request.getContent(), Timestamp.valueOf(LocalDateTime.now()), null);

      errorIfNull(complaint, "Failed to create complaint");

      response.setComplaintID(complaint.getId());
      response.setSuccess("Complaint created successfully");
      //TODO Tegra add complaint to statistics 
      return response;
    });
  }

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
      complaint.setResolvedAt(Timestamp.valueOf(LocalDateTime.now()));
      complaint.setStatus(Constants.COMPLAINT_STATUS_ACCEPTED);
      complaint.update(conn);

      response.setComplaintID(complaint.getId());
      response.setCustomerID(customer.getId());
      response.setAmount(action.getAmount());
      response.setSuccess("Refund completed successfully");
      return response;
    });
  }

  /**
   * Handle List My Complaints Request.
   *
   * @param request
   *          the request
   * @return the server response
   */
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
