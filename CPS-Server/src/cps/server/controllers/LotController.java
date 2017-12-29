package cps.server.controllers;

import java.util.Map;

import cps.api.action.InitLotAction;
import cps.api.response.ServerResponse;
import cps.common.Utilities.Holder;
import cps.server.ServerApplication;
import cps.server.devices.Robot;
import cps.entities.models.ParkingLot;

public class LotController extends RequestController {
	public LotController(ServerApplication serverApplication) {
		super(serverApplication);
	}

	@SuppressWarnings("unused")
	private Map<Integer, Robot> robots;
	
	public ServerResponse insertCar(ParkingLot lot, String carID) {
		return null; // return null if no error
	}
	
	public ServerResponse retrieveCar(ParkingLot lot, String carID) {
		return null; // return null if no error
	}
	
	public ServerResponse handle(InitLotAction request) {
		Holder<ParkingLot> result = new Holder<>(null);
		databaseController.performAction(conn -> {
			result.setValue(ParkingLot.create(conn, request.getStreetAddress(), request.getSize(), request.getPrice1(), request.getPrice2(), request.getRobotIP()));
		});

		return ServerResponse.decide("Entry creation", result.getValue() != null);	
	}
}
