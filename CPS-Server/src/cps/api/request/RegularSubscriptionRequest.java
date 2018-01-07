package cps.api.request;

import java.time.LocalDate;
import java.time.LocalTime;

import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.server.RequestHandler;

public class RegularSubscriptionRequest extends SubscriptionRequest {
	private static final long serialVersionUID = 1L;
	private int lotID;
	private LocalTime plannedExitTime;

	public RegularSubscriptionRequest(int customerID, String email, String carID, LocalDate startDate, int lotID,
			LocalTime endTime) {
		super(customerID, email, carID, startDate);
		this.lotID = lotID;
		this.plannedExitTime = endTime;
	}

	@Override
	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	public LocalTime getPlannedExitTime() {
		return plannedExitTime;
	}

	public void setPlannedExitTime(LocalTime endTime) {
		this.plannedExitTime = endTime;
	}

	@Override
	public ServerResponse handle(RequestHandler handler) {
		return handler.handle(this);
	}

	@Override
	public int getSubscriptionType() {
		return Constants.SUBSCRIPTION_TYPE_REGULAR;
	}
}
