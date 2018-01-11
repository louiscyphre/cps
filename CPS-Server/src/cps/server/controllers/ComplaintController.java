package cps.server.controllers;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import cps.api.request.ComplaintRequest;
import cps.api.response.ComplaintResponse;
import cps.api.response.ServerResponse;
import cps.entities.models.Complaint;
import cps.server.ServerController;
import cps.server.session.UserSession;

public class ComplaintController extends RequestController {

	public ComplaintController(ServerController serverController) {
		super(serverController);
	}

	public ServerResponse handle(ComplaintRequest request, UserSession session) {
		return databaseController.performQuery(conn -> {
			ComplaintResponse response = new ComplaintResponse();
			Complaint complaint = Complaint.create(conn, request.getCustomerID(), request.getContent(),
					Timestamp.valueOf(LocalDateTime.now()), null);

			if (complaint == null) {
				response.setError("Failed to create complaint");
				return response;
			}

			response.setComplaintID(complaint.getId());
			response.setSuccess("Complaint created successfully");
			return response;
		});
	}

}
