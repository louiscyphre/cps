package cps.server.controllers;

import cps.api.response.*;
import cps.api.request.FullSubscriptionRequest;
import cps.api.request.RegularSubscriptionRequest;
import cps.server.ServerController;

public class SubscriptionController extends RequestController {

	public SubscriptionController(ServerController serverController) {
		super(serverController);
	}
	
	public ServerResponse handle(FullSubscriptionRequest request) {
		return databaseController.performQuery(conn -> { // TODO: implement
			FullSubscriptionResponse response = new FullSubscriptionResponse(false, "");
//			SubscriptionService result = null;
			response.setSuccess("Full subscription created successfully");
			return response;
		});
	}
	
	public ServerResponse handle(RegularSubscriptionRequest request) {
		return databaseController.performQuery(conn -> { // TODO: implement
			RegularSubscriptionResponse response = new RegularSubscriptionResponse(false, "");
//			SubscriptionService result = null;
			response.setSuccess("Full subscription created successfully");
			return response;
		});
	}

}
