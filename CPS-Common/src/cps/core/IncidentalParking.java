package cps.core;

import java.io.Serializable;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import cps.common.Utilities;

public class IncidentalParking extends ParkingRequest {

	public IncidentalParking(int id, int customerID, int carID, Date estimatedExitTime, String email) {
		super(id, customerID, carID, estimatedExitTime, email);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {;		
		return "IncidentalParking(" + getId() + ", " + getCustomerID() + ", " + getCarID() + ", "
			+ Utilities.dateToString(getEstimatedExitTime()) + ", " + getEmail() + ")";
	}
}
