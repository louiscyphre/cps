package cps.api.request;

import java.time.LocalDateTime;

import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.server.RequestHandler;

public class IncidentalParkingRequest extends OnetimeParkingRequest {
	private static final long serialVersionUID = 1L;

	public IncidentalParkingRequest(int customerID, String email, String carID, int lotID, LocalDateTime plannedEndTime) {
		super(customerID, email, carID, lotID, plannedEndTime);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("{");
		buffer.append("customerID: " + getCustomerID() + ", ");
		buffer.append("email: " + getEmail() + ", ");
		buffer.append("carID: " + getCarID() + ", ");
		buffer.append("lotID: " + getLotID() + ", ");
		buffer.append("plannedEndTime: " + getPlannedEndTime() + "}");
		return buffer.toString();
	}

	@Override
	public ServerResponse handle(RequestHandler handler) {
		return handler.handle(this);
	}

	@Override
	public int getParkingType() {
		return Constants.PARKING_TYPE_INCIDENTAL;
	}
}
