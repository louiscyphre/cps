package cps.core;

import java.sql.Date;

public class ReservedParking extends ParkingRequest {

	private int lotID;
	private Date startTime;

	public ReservedParking(int id, int customerID, int carID, Date estimatedExitTime, String email, int lotID,
			Date startTime) {
		super(id, customerID, carID, estimatedExitTime, email);
		this.lotID = lotID;
		this.startTime = startTime;
	}

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

}
