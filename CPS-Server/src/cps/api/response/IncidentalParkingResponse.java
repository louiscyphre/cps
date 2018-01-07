package cps.api.response;

public class IncidentalParkingResponse extends OnetimeParkingResponse {
	private static final long serialVersionUID = 1L;
	
	public IncidentalParkingResponse(boolean success, String description, int customerID, String password, int serviceID) {
		super(success, description, customerID, password, serviceID);
	}
	
	public IncidentalParkingResponse(boolean success, String description) {
		this(success, description, 0, "", 0);
	}
}
