package cps.api.request;

import cps.api.response.ServerResponse;
import cps.server.session.UserSession;

public class ParkingExitRequest extends CustomerRequest {
	private static final long serialVersionUID = 1L;

	private int lotID;
	private String carID;

	public ParkingExitRequest(int customerID, int lotID, String carID) {
		super(customerID);
		this.lotID = lotID;
		this.carID = carID;
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
	public ServerResponse handle(RequestHandler handler, UserSession session) {
		return handler.handle(this, session);
	}
}
