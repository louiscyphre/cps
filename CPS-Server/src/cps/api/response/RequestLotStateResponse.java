package cps.api.response;

import cps.entities.models.ParkingLot;

public class RequestLotStateResponse extends ServerResponse {
	private static final long serialVersionUID = 1L;
	private ParkingLot lot;

	public RequestLotStateResponse(boolean success, String description, ParkingLot lot) {
		super(success, description);
		this.lot = lot;
	}

	public ParkingLot getLot() {
		return lot;
	}

	public void setLot(ParkingLot lot) {
		this.lot = lot;
	}

}
