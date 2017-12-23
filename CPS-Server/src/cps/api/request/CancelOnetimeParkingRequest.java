package cps.api.request;

public class CancelOnetimeParkingRequest extends ClientRequest {
	/*public CancelOnetimeParkingRequest(int customerID, int onetimeServiceID) {
		super(customerID);
		this.onetimeServiceID = onetimeServiceID;
	}*/

	private static final long serialVersionUID = 1L;
	private int onetimeServiceID;

	public int getOnetimeServiceID() {
		return onetimeServiceID;
	}

	public void setOnetimeServiceID(int onetimeServiceID) {
		this.onetimeServiceID = onetimeServiceID;
	}
}
