package cps.api.request;

import cps.api.response.ServerResponse;

public class CancelOnetimeParkingRequest extends CustomerRequest {
	public CancelOnetimeParkingRequest(int customerID, int onetimeServiceID) {
		super(customerID);
		this.onetimeServiceID = onetimeServiceID;
	}

	private static final long serialVersionUID = 1L;
	private int onetimeServiceID;

	public int getOnetimeServiceID() {
		return onetimeServiceID;
	}

	public void setOnetimeServiceID(int onetimeServiceID) {
		this.onetimeServiceID = onetimeServiceID;
	}

	@Override
	public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
		return handler.handle(this, session);
	}
}
