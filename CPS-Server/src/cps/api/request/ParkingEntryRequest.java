package cps.api.request;

import cps.api.response.ServerResponse;

public class ParkingEntryRequest extends CustomerRequest {
	private static final long serialVersionUID = 1L;

	private int subscriptionID;
	private int lotID;
	private String carID;

	public ParkingEntryRequest(int customerID, int subscriptionID, int lotID, String carID) {
		super(customerID);
		this.subscriptionID = subscriptionID;
		this.lotID = lotID;
		this.carID = carID;
	}

	public int getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(int subscriptionID) {
		this.subscriptionID = subscriptionID;
	}

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	public String getCarID() {
		return carID;
	}

	public void setCarID(String carID) {
		this.carID = carID;
	}

	@Override
	public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
		return handler.handle(this, session);
	}
}
