package cps.core;

import java.sql.Date;

public class IncidentalParking extends ParkingRequest {

	public IncidentalParking(int id, int customerID, int carID, Date estimatedExitTime, String email) {
		super(id, customerID, carID, estimatedExitTime, email);
		// TODO Auto-generated constructor stub
	}
}
