package cps.entities.models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CarTransportation implements Serializable {

	private static final long serialVersionUID = 1L;
	/* key - customer id + car id + lot id + inserted at */
	private int customerID;
	private String carID;
	private int authType;
	private int lotID;
	
	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getCarID() {
		return carID;
	}

	public void setCarID(String carID) {
		this.carID = carID;
	}

	public int getAuthType() {
		return authType;
	}

	public void setAuthType(int authType) {
		this.authType = authType;
	}

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	public Timestamp getInsertedAt() {
		return insertedAt;
	}

	public void setInsertedAt(Timestamp insertedAt) {
		this.insertedAt = insertedAt;
	}

	public Timestamp getRemovedAt() {
		return removedAt;
	}

	public void setRemovedAt(Timestamp removedAt) {
		this.removedAt = removedAt;
	}

	public CarTransportation(int customerID, String carID, int authType, int lotID, Timestamp insertedAt,
			Timestamp removedAt) {
		this.customerID = customerID;
		this.carID = carID;
		this.authType = authType;
		this.lotID = lotID;
		this.insertedAt = insertedAt;
		this.removedAt = removedAt;
	}

	private Timestamp insertedAt;
	private Timestamp removedAt;

	public CarTransportation(ResultSet rs) throws SQLException {
		this(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getTimestamp(5), rs.getTimestamp(6));
	}

}
