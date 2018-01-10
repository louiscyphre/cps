package cps.entities.models;

import java.nio.channels.NonWritableChannelException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.LinkedList;

import cps.common.Constants;

import java.sql.PreparedStatement;

// Database entity for monthly parking subscriptions - regular or full both stored in the same table.

public class SubscriptionService implements ParkingService {
	private static final long serialVersionUID = 1L;

	int id;
	int subscriptionType; // 1 = regular, 2 = full
	String email;
	int customerID;
	String carID;
	int lotID; // if null then full, else regular
	LocalDate startDate;
	LocalDate endDate;
	LocalTime dailyExitTime; // null for full subscription

	public SubscriptionService(int id, int type, int customerID, String email, String carID, int lotID,
			LocalDate startDate, LocalDate endDate, LocalTime dailyExitTime) {
		this.id = id;
		this.subscriptionType = type;
		this.customerID = customerID;
		this.email = email;
		this.carID = carID;
		this.lotID = lotID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dailyExitTime = dailyExitTime;
	}

	public SubscriptionService(ResultSet rs) throws SQLException {
		this(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getInt(6),
				rs.getDate(7).toLocalDate(), rs.getDate(8).toLocalDate(), rs.getTime(9).toLocalTime());
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

	public String getCarID() {
		return carID;
	}

	public void setCarID(String carID) {
		this.carID = carID;
	}

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalTime getDailyExitTime() {
		return dailyExitTime;
	}

	public void setDailyExitTime(LocalTime endTime) {
		this.dailyExitTime = endTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int getLicenseType() {
		return Constants.LICENSE_TYPE_SUBSCRIPTION;
	}

	public static SubscriptionService create(Connection conn, int type, int customerID, String email, String carID,
			int lotID, LocalDate startDate, LocalDate endDate, LocalTime dailyExitTime) throws SQLException {
		PreparedStatement st = conn.prepareStatement(Constants.SQL_CREATE_SUBSCRIPTION_SERVICE,
				Statement.RETURN_GENERATED_KEYS);

		int field = 1;
		st.setInt(field++, type);
		st.setInt(field++, customerID);
		st.setString(field++, email);
		st.setString(field++, carID);
		st.setInt(field++, lotID);
		st.setDate(field++, Date.valueOf(startDate));
		st.setDate(field++, Date.valueOf(endDate));
		st.setTime(field++, Time.valueOf(dailyExitTime));
		st.executeUpdate();

		ResultSet keys = st.getGeneratedKeys();
		int newID = 0;

		if (keys != null && keys.next()) {
			newID = keys.getInt(1);
			keys.close();
		}

		st.close();

		return new SubscriptionService(newID, type, customerID, email, carID, lotID, startDate, endDate, dailyExitTime);
	}

	public static SubscriptionService findForEntry(Connection conn, int customerID, String carID, int subsID)
			throws SQLException {
		SubscriptionService result = null;

		PreparedStatement st = conn.prepareStatement(Constants.SQL_GET_SUBSCRIPTION_BY_ID_CUSTOMER_CAR);

		int index = 1;

		st.setInt(index++, subsID);
		st.setInt(index++, customerID);
		st.setString(index++, carID);

		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			result = new SubscriptionService(rs);
		}
		rs.close();
		st.close();

		return result;
	}

	public static Collection<SubscriptionService> findByCustomerID(Connection conn, int customerID)
			throws SQLException {
		LinkedList<SubscriptionService> results = new LinkedList<SubscriptionService>();

		PreparedStatement stmt = conn.prepareStatement(Constants.GET_SUBSCRIPTION_SERVICE_BY_CUSTOMER_ID);
		stmt.setInt(1, customerID);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			results.add(new SubscriptionService(rs));
		}

		rs.close();
		stmt.close();

		return results;
	}

	@Override
	public LocalDateTime getExitTime() {
		if (this.subscriptionType == Constants.SUBSCRIPTION_TYPE_FULL) {
			return LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		}
		return this.dailyExitTime.atDate(LocalDate.now());
	}

	public static ParkingService findByID(Connection conn, int authID) throws SQLException {
		ParkingService results = null;

		PreparedStatement stmt = conn.prepareStatement(Constants.GET_SUBSCRIPTION_SERVICE_BY_ID);
		stmt.setInt(1, authID);
		ResultSet rs = stmt.executeQuery();

		results = new SubscriptionService(rs);

		rs.close();
		stmt.close();

		return results;

	}
}
