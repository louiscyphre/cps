package cps.api.request;

import java.time.LocalDateTime;
import cps.common.Utilities;

public class IncidentalParkingRequest extends OnetimeParkingRequest {
	private static final long serialVersionUID = 1L;
	public static final int TYPE = 1;

	public IncidentalParkingRequest(int customerID, String email, String carID, int lotID, LocalDateTime plannedEndTime) {
		super(customerID, email, carID, lotID, plannedEndTime);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {;		
		return "IncidentalParking(" + getCustomerID() + ", " + getCarID() + ", "
			+ Utilities.dateToString(getPlannedEndTime()) + ", " + getEmail() + ")";
	}
}
