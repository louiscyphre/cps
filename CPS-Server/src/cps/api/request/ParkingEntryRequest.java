package cps.api.request;

import java.time.LocalDateTime;

public class ParkingEntryRequest extends CustomerRequest {
	private static final long serialVersionUID = 1L;

	private LocalDateTime entryTime;
	private int entryLicenseID;
	private int entryLicenseType;
	private int lotID;
	private int carID;

	public ParkingEntryRequest(int customerID, LocalDateTime entryTime, int entryLicenseID, int entryLicenseType, int lotID,
			int carID) {
		super(customerID);
		this.entryTime = entryTime;
		this.entryLicenseID = entryLicenseID;
		this.entryLicenseType = entryLicenseType;
		this.lotID = lotID;
		this.carID = carID;
	}

	public LocalDateTime getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(LocalDateTime entryTime) {
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
