package cps.api.response;

public class ReservedParkingResponse extends OnetimeParkingResponse {
	private static final long serialVersionUID = 1L;
	
	public ReservedParkingResponse(boolean success, int customerID, String password, int serviceID) {
		super(success, "ReservedParkingRequest", customerID, password, serviceID);
	}
}
