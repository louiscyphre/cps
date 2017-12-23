package cps.entities.models;

import java.io.Serializable;
import java.sql.Timestamp;
// Database entity for monthly parking subscriptions - regular or full both stored in the same table .

public class SubscriptionService implements Serializable {
	private static final long serialVersionUID = 1L;

	int id;
	int type; // 1 = regular, 2 = full
	int customerID;
	int carID;
	int lotID; // if null then full, else regular
	Timestamp startDate;
	Timestamp endDate;
	Timestamp endTime; // null for full subscription
	String email;

	/*public SubscriptionService(int id, int type, int customerID, int carID, int lotID, Timestamp startDate,
			Timestamp endDate, Timestamp endTime, String email) {
		this.id = id;
		this.type = type;
		this.customerID = customerID;
		this.carID = carID;
		this.lotID = lotID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.endTime = endTime;
		this.email = email;
	}

	public SubscriptionService(ResultSet rs) throws SQLException {
		this(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getTimestamp(6),
				rs.getTimestamp(7), rs.getTimestamp(8), rs.getString(9));
	}*/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
