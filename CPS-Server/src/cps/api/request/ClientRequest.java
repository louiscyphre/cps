package cps.api.request;

import java.io.Serializable;

public abstract class ClientRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int customerID;

	/*public ClientRequest(int customerID) {
		this.customerID = customerID;
	}*/

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

}
