package cps.server.controllers;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import cps.api.action.RefundAction;
import cps.api.request.ComplaintRequest;
import cps.api.response.ComplaintResponse;
import cps.api.response.RefundResponse;
import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.entities.models.Complaint;
import cps.entities.models.Customer;
import cps.entities.people.User;
import cps.server.ServerController;
import cps.server.session.UserSession;

public class ComplaintController extends RequestController {

	public ComplaintController(ServerController serverController) {
		super(serverController);
	}

	public ServerResponse handle(ComplaintRequest request, UserSession session) {
		return database.performQuery(new ComplaintResponse(), (conn, response) -> {
			Complaint complaint = Complaint.create(conn, request.getCustomerID(), request.getContent(),
					Timestamp.valueOf(LocalDateTime.now()), null);
			
			errorIfNull(complaint, "Failed to create complaint");
			
			response.setComplaintID(complaint.getId());
			response.setSuccess("Complaint created successfully");
			return response;
		});
	}

	public ServerResponse handle(RefundAction action, UserSession session) {
		return database.performQuery(new RefundResponse(), (conn, response) -> {
			User employee = session.requireUser(); // TODO check access level
			
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

}
