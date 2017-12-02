package cps.core;

import java.sql.Date;

public class RegularSubscription extends SubscriptionRequest {

	private int lotID;
	private Date endTime;

	public RegularSubscription(int id, int customerID, int customerID2, int carID, Date startDate, int lotID,
			Date endTime) {
		super(id, customerID, customerID2, carID, startDate);
		this.lotID = lotID;
		this.endTime = endTime;
	}

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
