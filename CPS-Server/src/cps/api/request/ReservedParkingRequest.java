package cps.api.request;

import java.time.LocalDateTime;

import cps.api.response.ServerResponse;
import cps.common.Constants;

public class ReservedParkingRequest extends OnetimeParkingRequest {
	private static final long serialVersionUID = 1L;
	private LocalDateTime plannedStartTime;

	public ReservedParkingRequest(int customerID, String email, String carID, int lotID, LocalDateTime plannedStartTime,
			LocalDateTime plannedEndTime) {
		super(customerID, email, carID, lotID, plannedEndTime);
		this.plannedStartTime = plannedStartTime;
	}

	public LocalDateTime getPlannedStartTime() {
		return plannedStartTime;
	}

	public void setPlannedStartTime(LocalDateTime plannedStartTime) {
		this.plannedStartTime = plannedStartTime;
	}

	@Override
	public ServerResponse handle(RequestHandler handler) {
		return handler.handle(this);
	}

	@Override
	public int getParkingType() {
		return Constants.PARKING_TYPE_RESERVED;
	}

}
