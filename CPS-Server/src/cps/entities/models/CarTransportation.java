package cps.entities.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.Timestamp;

public class CarTransportation extends Entity {

	private static final long serialVersionUID = 1L;
	/* key - customer id + car id + lot id + inserted at */
	private int customerId;
	private String carId;
	private int authType;
	private int lotId;
	private Timestamp insertedAt;
	private Timestamp removedAt;

	public CarTransportation(int customerId, String carId, int authType, int lotId, Timestamp insertedAt,
			Timestamp removedAt) {
		this.customerId = customerId;
		this.carId = carId;
		this.authType = authType;
		this.lotId = lotId;
		this.insertedAt = insertedAt;
		this.removedAt = removedAt;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public int getAuthType() {
		return authType;
	}

	public void setAuthType(int authType) {
		this.authType = authType;
	}

	public int getLotId() {
		return lotId;
	}

	public void setLotId(int lotId) {
		this.lotId = lotId;
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

	public static CarTransportation buildFromQueryResult(ResultSet rs) throws SQLException {
		return new CarTransportation(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getTimestamp(5),
				rs.getTimestamp(6));
	}

}
