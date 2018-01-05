package cps.api.request;

import java.time.LocalDateTime ;

import cps.api.response.ServerResponse;
import cps.server.RequestHandler;

public class ParkingExitRequest extends CustomerRequest {
	private static final long serialVersionUID = 1L;

	private LocalDateTime  exitTime;
	private int lotID;
	private int carID;

	public ParkingExitRequest(int customerID, LocalDateTime  exitTime, int lotID, int carID) {
		super(customerID);
		this.exitTime = exitTime;
		this.lotID = lotID;
		this.carID = carID;
	}

	public LocalDateTime  getExitTime() {
		return exitTime;
	}

	public void setExitTime(LocalDateTime  exitTime) {
		this.exitTime = exitTime;
	}

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	public int getCarID() {
		return carID;
	}

	public void setCarID(int carID) {
		this.carID = carID;
	}

	@Override
	public ServerResponse handle(RequestHandler handler) {
		return handler.handle(this);
	}
}
