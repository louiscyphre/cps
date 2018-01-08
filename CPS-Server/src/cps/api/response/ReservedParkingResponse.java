package cps.api.response;

public class ReservedParkingResponse extends OnetimeParkingResponse {
	private static final long serialVersionUID = 1L;
	
	public ReservedParkingResponse(boolean success, String description, int customerID, String password, int serviceID) {
		super(success, description, customerID, password, serviceID);
	}
	
	public ReservedParkingResponse(boolean success, String description) {
		super(success, description, 0, "", 0);
	}
	
  @Override
  public ServerResponse handle(ResponseHandler handler) {
    return handler.handle(this);
  }
}
