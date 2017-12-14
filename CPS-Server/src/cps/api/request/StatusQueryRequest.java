package cps.api.request;

public class StatusQueryRequest extends ClientRequest {
	private static final long serialVersionUID = 1L;
	
	public StatusQueryRequest(int customerID) {
		super(customerID);
	}
}
