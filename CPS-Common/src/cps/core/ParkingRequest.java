package cps.core;

import java.sql.Date;

public abstract class ParkingRequest extends CustomerAction {

	private int carID;
	private Date estimatedExitTime;
	private String email;

	public ParkingRequest(int id, int customerID, int carID, Date estimatedExitTime, String email) {
		super(id, customerID);
		this.carID = carID;
		this.estimatedExitTime = estimatedExitTime;
		this.email = email;
	}

	public int getCarID() {
		return carID;
	}

	public void setCarID(int carID) {
		this.carID = carID;
	}

	public Date getEstimatedExitTime() {
		return estimatedExitTime;
	}

	public void setEstimatedExitTime(Date estimatedExitTime) {
		this.estimatedExitTime = estimatedExitTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
