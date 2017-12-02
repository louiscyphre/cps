package cps.core;

public abstract class CustomerAction {

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
