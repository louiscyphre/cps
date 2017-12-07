package cps.core;

import java.time.LocalDateTime;

public class ReservedParking extends ParkingRequest {
	private static final long serialVersionUID = 1L;

	private int lotID;
	private LocalDateTime startTime;

	public ReservedParking(int id, int customerID, int carID, LocalDateTime plannedEndTime, String email, int lotID,
			LocalDateTime startTime) {
		super(id, customerID, carID, plannedEndTime, email);
		this.lotID = lotID;
		this.startTime = startTime;
	}

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

}
