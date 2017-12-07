package cps.core;

import java.time.LocalDateTime;
import cps.common.Utilities;

public class IncidentalParking extends ParkingRequest {
	private static final long serialVersionUID = 1L;
	public static final int TYPE = 1;

	public IncidentalParking(int id, int customerID, String email, int carID, int lotID, LocalDateTime plannedEndTime) {
		super(id, customerID, email, carID, lotID, plannedEndTime);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {;		
		return "IncidentalParking(" + getId() + ", " + getCustomerID() + ", " + getCarID() + ", "
			+ Utilities.dateToString(getPlannedEndTime()) + ", " + getEmail() + ")";
	}
}
