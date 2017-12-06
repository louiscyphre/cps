package cps.core;

import java.sql.Date;

public abstract class SubscriptionRequest extends CustomerAction {

	private int customerID;
	private int carID;
	private Date startDate;

	public SubscriptionRequest(int id, int customerID, int customerID2, int carID, Date startDate) {
		super(id, customerID);
		customerID = customerID2;
		this.carID = carID;
		this.startDate = startDate;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public int getCarID() {
		return carID;
	}

	public void setCarID(int carID) {
		this.carID = carID;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
