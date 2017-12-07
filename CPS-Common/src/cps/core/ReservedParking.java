package cps.core;

import java.time.LocalDateTime;

public class ReservedParking extends ParkingRequest {
	private static final long serialVersionUID = 1L;
	private LocalDateTime startTime;

	public ReservedParking(int id, int customerID, String email, int carID, int lotID, LocalDateTime plannedEndTime,
			LocalDateTime startTime) {
		super(id, customerID, email, carID, lotID, plannedEndTime);
		this.startTime = startTime;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

}
