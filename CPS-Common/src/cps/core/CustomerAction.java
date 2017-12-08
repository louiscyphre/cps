package cps.core;

import java.io.Serializable;

public abstract class CustomerAction implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int customerID;

	public CustomerAction(int id, int customerID) {
		super();
		this.id = id;
		this.customerID = customerID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

}
