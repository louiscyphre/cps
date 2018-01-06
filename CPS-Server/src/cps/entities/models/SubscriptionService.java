package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import cps.common.Constants;

import java.sql.PreparedStatement;

// Database entity for monthly parking subscriptions - regular or full both stored in the same table .

public class SubscriptionService implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int LICENSE_TYPE = Constants.LICENSE_TYPE_SUBSCRIPTION;

	int id;
	int subscriptionType; // 1 = regular, 2 = full
	int customerID;
	int carID;
	int lotID; // if null then full, else regular
	Timestamp startDate;
	Timestamp endDate;
	Timestamp endTime; // null for full subscription
	String email;

	public SubscriptionService(int id, int type, int customerID, int carID, int lotID, Timestamp startDate,
			Timestamp endDate, Timestamp endTime, String email) {
		this.id = id;
		this.subscriptionType = type;
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
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(int type) {
		this.subscriptionType = type;
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

	public static SubscriptionService findForEntry(Connection conn, int customerID, String carID, int subsID)
			throws SQLException {
		//TODO : Cauchy check this
		SubscriptionService result = null;
		ResultSet rs = null;
		PreparedStatement _ps = conn.prepareStatement(Constants.SQL_GET_SUBSCRIPTION_BY_ID_CUSTOMER_CAR);
		int index = 1;
		
		_ps.setInt(index++, subsID);
		_ps.setInt(index++, customerID);
		_ps.setString(index++, carID);
		
		rs = _ps.executeQuery();
		_ps.close();
		
		result = new SubscriptionService(rs);
		return result;
	}

}
