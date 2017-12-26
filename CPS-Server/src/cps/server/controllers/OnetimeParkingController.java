package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import cps.api.request.IncidentalParkingRequest;
import cps.api.request.ListOnetimeEntriesRequest;
import cps.api.request.ReservedParkingRequest;
import cps.api.response.ListOnetimeEntriesResponse;
import cps.api.response.ServerResponse;
import cps.entities.models.OnetimeService;
import cps.server.ServerApplication;
import cps.common.Utilities.Holder;

@SuppressWarnings("unused")
public class OnetimeParkingController extends RequestController {	
	public OnetimeParkingController(ServerApplication serverApplication) {
		super(serverApplication);
	}

	public ServerResponse handle(IncidentalParkingRequest request) {
		Holder<OnetimeService> result = new Holder<>(null);
		databaseController.performAction(conn -> {
			Timestamp startTime = new Timestamp(System.currentTimeMillis());
			Timestamp plannedEndTime = Timestamp.valueOf(request.getPlannedEndTime());
			result.setValue(OnetimeService.create(conn, IncidentalParkingRequest.TYPE, request.getCustomerID(),
					request.getEmail(), request.getCarID(), request.getLotID(), startTime,
					plannedEndTime, false));
		});

		// System.out.println(result.getValue());
		return ServerResponse.decide("Entry creation", result.getValue() != null);		
	}
	
	public ServerResponse handle(ReservedParkingRequest request) {
		return ServerResponse.error("Not implemented"); // TODO: implement		
	}
	
	public ServerResponse handle(ListOnetimeEntriesRequest request) {
		Holder<Collection<OnetimeService>> result = new Holder<>(null);
		databaseController.performAction(conn -> result.setValue(OnetimeService.findByCustomerID(conn, request.getCustomerID())));		
		System.out.println(result.getValue());
		
		if (result.getValue() == null) {
			return ServerResponse.error("Entry retrieval failed");
		}
		
		return new ListOnetimeEntriesResponse("Entry retrieval successful", result.getValue(), request.getCustomerID());	
	}
}