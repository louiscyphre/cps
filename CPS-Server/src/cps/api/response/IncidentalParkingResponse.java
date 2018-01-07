package cps.api.response;

public class IncidentalParkingResponse extends OnetimeParkingResponse {
	private static final long serialVersionUID = 1L;
	
	public IncidentalParkingResponse(boolean success, int customerID, String password, int serviceID) {
		super(success, "IncidentalParkingRequest", customerID, password, serviceID);
	}
}
