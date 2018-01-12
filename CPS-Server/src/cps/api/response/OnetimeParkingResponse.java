package cps.api.response;

public abstract class OnetimeParkingResponse extends CustomerPasswordResponse {
	private static final long serialVersionUID = 1L;

	private int serviceID;

	public OnetimeParkingResponse(boolean success, String description, int customerID, String password, int serviceID) {
		super(success, description, customerID, password);
		this.serviceID = serviceID;
	}

	public int getServiceID() {
		return serviceID;
	}

	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}

	@Override
	public ServerResponse handle(ResponseHandler handler) {
		return handler.handle(this);
	}
}
