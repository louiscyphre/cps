package cps.api.request;

import java.time.LocalDateTime;

import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.server.RequestHandler;

public class RegularSubscriptionRequest extends SubscriptionRequest {
	private static final long serialVersionUID = 1L;
	private int lotID;
	private LocalDateTime endTime;

	public RegularSubscriptionRequest(int customerID, int carID, LocalDateTime startDate, int lotID,
			LocalDateTime endTime) {
		super(customerID, carID, startDate);
		this.lotID = lotID;
		this.endTime = endTime;
	}

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
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
