package cps.api.request;

import java.time.LocalDateTime;

public abstract class SubscriptionRequest extends ClientRequest {
	private static final long serialVersionUID = 1L;
	private int customerID;
	private int carID;
	private LocalDateTime startDate;

	/*public SubscriptionRequest(int customerID, int carID, LocalDateTime startDate) {
		super(customerID);
		this.carID = carID;
		this.startDate = startDate;
	}*/

	@Override
	public int getCustomerID() {
		return customerID;
	}

	@Override
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public int getCarID() {
		return carID;
	}

	public void setCarID(int carID) {
		this.carID = carID;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

}
