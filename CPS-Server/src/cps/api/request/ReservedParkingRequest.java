package cps.api.request;

import java.time.LocalDateTime;

import cps.api.response.ServerResponse;
import cps.server.RequestHandler;

public class ReservedParkingRequest extends OnetimeParkingRequest {
	private static final long serialVersionUID = 1L;
	private LocalDateTime startTime;

	public ReservedParkingRequest(int customerID, String email, String carID, int lotID, LocalDateTime plannedEndTime,
			LocalDateTime startTime) {
		super(customerID, email, carID, lotID, plannedEndTime);
		this.startTime = startTime;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	@Override
	public ServerResponse handle(RequestHandler handler) {
		return handler.handle(this);
	}

}
