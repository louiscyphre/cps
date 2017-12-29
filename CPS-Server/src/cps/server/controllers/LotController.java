package cps.server.controllers;

import java.util.Map;

import cps.api.response.ServerResponse;
import cps.server.devices.Robot;
import cps.entities.models.ParkingLot;

public class LotController {
	@SuppressWarnings("unused")
	private Map<Integer, Robot> robots;
	
	public ServerResponse insertCar(ParkingLot lot, String carID) {
		return null; // return null if no error
	}
	
	public ServerResponse retrieveCar(ParkingLot lot, String carID) {
		return null; // return null if no error
	}
}
