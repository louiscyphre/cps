package cps.api.request;

import java.time.LocalDateTime;

public abstract class OnetimeParkingRequest extends CustomerRequest {
	private static final long serialVersionUID = 1L;

	private String email;
	private String carID;
	private int lotID;
	private LocalDateTime plannedEndTime;

	public OnetimeParkingRequest(int customerID, String email, String carID, int lotID, LocalDateTime plannedEndTime) {
		super(customerID);
		this.email = email;
		this.carID = carID;
		this.lotID = lotID;
		this.plannedEndTime = plannedEndTime;
	}

	public String getCarID() {
		return carID;
	}

	public void setCarID(String carID) {
		this.carID = carID;
	}

	public LocalDateTime getPlannedEndTime() {
		return plannedEndTime;
	}

	public void setPlannedEndTime(LocalDateTime plannedEndTime) {
		this.plannedEndTime = plannedEndTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	public abstract int getParkingType();

}
