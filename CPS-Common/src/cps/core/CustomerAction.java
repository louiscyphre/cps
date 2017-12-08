package cps.core;

import java.io.Serializable;

public abstract class CustomerAction implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int customerID;

	public CustomerAction(int customerID) {
		this.customerID = customerID;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

}
