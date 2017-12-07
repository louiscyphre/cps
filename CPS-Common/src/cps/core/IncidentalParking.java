package cps.core;

import java.time.LocalDateTime;
import cps.common.Utilities;

public class IncidentalParking extends ParkingRequest {
	private static final long serialVersionUID = 1L;

	public IncidentalParking(int id, int customerID, int carID, LocalDateTime plannedEndTime, String email) {
		super(id, customerID, carID, plannedEndTime, email);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {;		
		return "IncidentalParking(" + getId() + ", " + getCustomerID() + ", " + getCarID() + ", "
			+ Utilities.dateToString(getPlannedEndTime()) + ", " + getEmail() + ")";
	}
}
