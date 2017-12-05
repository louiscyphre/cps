package cps.core;

import java.sql.Date;

public class ParkingEntry extends CustomerAction {

	private Date entryTime;
	private int entryLicenseID;
	private int entryLicenseType;
	private int lotID;
	private int carID;

	public ParkingEntry(int id, int customerID, Date entryTime, int entryLicenseID, int entryLicenseType, int lotID,
			int carID) {
		super(id, customerID);
		this.entryTime = entryTime;
		this.entryLicenseID = entryLicenseID;
		this.entryLicenseType = entryLicenseType;
		this.lotID = lotID;
		this.carID = carID;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public int getEntryLicenseID() {
		return entryLicenseID;
	}

	public void setEntryLicenseID(int entryLicenseID) {
		this.entryLicenseID = entryLicenseID;
	}

	public int getEntryLicenseType() {
		return entryLicenseType;
	}

	public void setEntryLicenseType(int entryLicenseType) {
		this.entryLicenseType = entryLicenseType;
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
