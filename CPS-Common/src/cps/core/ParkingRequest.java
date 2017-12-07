package cps.core;

import java.time.LocalDateTime;

public abstract class ParkingRequest extends CustomerAction {
	private static final long serialVersionUID = 1L;

	private int carID;
	private LocalDateTime plannedEndTime;
	private String email;

	public ParkingRequest(int id, int customerID, int carID, LocalDateTime plannedEndTime, String email) {
		super(id, customerID);
		this.carID = carID;
		this.plannedEndTime = plannedEndTime;
		this.email = email;
	}

	public int getCarID() {
		return carID;
	}

	public void setCarID(int carID) {
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

}
