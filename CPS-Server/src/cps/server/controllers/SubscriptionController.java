package cps.server.controllers;

import cps.api.request.FullSubscriptionRequest;
import cps.api.request.RegularSubscriptionRequest;
import cps.api.response.*;
import cps.entities.models.SubscriptionService;
import cps.server.ServerController;

public class SubscriptionController extends RequestController {

	public SubscriptionController(ServerController serverController) {
		super(serverController);
	}
	
	public ServerResponse handle(FullSubscriptionRequest request) {
		return databaseController.performQuery(conn -> {
			SubscriptionService result = null;
			// TODO: implement FullSubscriptionRequest
			return ServerResponse.decide("Entry creation", result != null);
		});
	}
	
	public ServerResponse handle(RegularSubscriptionRequest request) {
		return ServerResponse.error("Not implemented");
// TODO: implement RegularSubscriptionRequest
	}

}
