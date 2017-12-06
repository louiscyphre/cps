package cps.core;

import java.sql.Date;

public class ParkingExit extends CustomerAction {

	private Date exitTime;
	private int lotID;
	private int carID;

	public ParkingExit(int id, int customerID, Date exitTime, int lotID, int carID) {
		super(id, customerID);
		this.exitTime = exitTime;
		this.lotID = lotID;
		this.carID = carID;
	}

	public Date getExitTime() {
		return exitTime;
	}

	public void setExitTime(Date exitTime) {
		this.exitTime = exitTime;
	}

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	public int getCarID() {
		return carID;
	}

	public void setCarID(int carID) {
		this.carID = carID;
	}

}
