package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import cps.common.Constants;
import javafx.util.converter.LocalDateStringConverter;

// TODO: Auto-generated Javadoc
/**
 * The Class DailyStatistics.
 */
public class DailyStatistics implements Serializable {
	// `day` date NOT NULL,
	// `realized_orders` int(11) DEFAULT NULL,
	// `canceled_orders` int(11) DEFAULT NULL,
	// `late_arrivals` int(11) DEFAULT NULL,
	// `complaints` int(11) DEFAULT NULL,
	// PRIMARY KEY (`day`)

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The day. */
	private LocalDate day;
	
	/** The realized orders. */
	private int realizedOrders;
	
	/** The canceled orders. */
	private int canceledOrders;
	
	/** The late arrivals. */
	private int lateArrivals;
	
	/** The complaints. */
	private int complaints;

	/**
	 * Instantiates a new daily statistics.
	 *
	 * @param day the day
	 * @param realizedOrders the realized orders
	 * @param canceledOrders the canceled orders
	 * @param lateArrivals the late arrivals
	 * @param complaints the complaints
	 */
	public DailyStatistics(LocalDate day, int realizedOrders, int canceledOrders, int lateArrivals, int complaints) {
		super();
		this.day = day;
		this.realizedOrders = realizedOrders;
		this.canceledOrders = canceledOrders;
		this.lateArrivals = lateArrivals;
		this.complaints = complaints;
	}

	/**
	 * Instantiates a new daily statistics.
	 *
	 * @param rs the rs
	 * @throws SQLException the SQL exception
	 */
	public DailyStatistics(ResultSet rs) throws SQLException {
		this(rs.getDate(1).toLocalDate(), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));
	}

	/**
	 * Creates the.
	 *
	 * @param conn the conn
	 * @param lotId the lot id
	 * @return the daily statistics
	 * @throws SQLException the SQL exception
	 */
	public static DailyStatistics create(Connection conn, int lotId) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(Constants.SQL_CREATE_NEW_DAY);

		LocalDate today = LocalDate.now();
		int field = 1;
		stmt.setDate(field++, Date.valueOf(today));
		stmt.setInt(field++, lotId);
		stmt.executeUpdate();

		stmt.close();
		return new DailyStatistics(today, lotId, 0, 0, 0);

	}

	/**
	 * Increase realized order count by one for today in specific parking lot.
	 *
	 * @param conn the conn
	 * @param lotId the lot id
	 * @throws SQLException the SQL exception
	 */
	public static void IncreaseRealizedOrder(Connection conn, int lotId) throws SQLException {
		IncreaseRealizedOrder(conn, LocalDate.now(), lotId);
	}

	/**
	 * Increase realized order count by one in specific parking lot at specific date.
	 *
	 * @param conn the conn
	 * @param _date the date
	 * @param lotId the lot id
	 * @throws SQLException the SQL exception
	 */
	public static void IncreaseRealizedOrder(Connection conn, LocalDate _date, int lotId) throws SQLException {
		PreparedStatement qwry = conn.prepareStatement(Constants.CHECK_DATE);
		int index = 1;
		int _order = 0;
		qwry.setDate(index, Date.valueOf(_date));
		ResultSet rs = qwry.executeQuery();
		if (rs.wasNull())
			create(conn, lotId);
		else
			_order = rs.getInt("realized_orders") + 1;
		qwry.close();
		PreparedStatement stmt = conn.prepareStatement(Constants.INCREASE_REALIZED_ORDER);
		stmt.setInt(index++, _order);
		stmt.setDate(index++, Date.valueOf(_date));
		stmt.setInt(index++, lotId);
		if (stmt.executeUpdate() == 0)
			throw new SQLException("Failed to increase Realized Order Count");
		stmt.close();
	}

	/**
	 * Gets the day.
	 *
	 * @return the day
	 */
	public LocalDate getDay() {
		return day;
	}

	/**
	 * Sets the day.
	 *
	 * @param day the new day
	 */
	public void setDay(LocalDate day) {
		this.day = day;
	}

	/**
	 * Gets the realized orders.
	 *
	 * @return the realized orders
	 */
	public int getRealizedOrders() {
		return realizedOrders;
	}

	/**
	 * Sets the realized orders.
	 *
	 * @param realizedOrders the new realized orders
	 */
	public void setRealizedOrders(int realizedOrders) {
		this.realizedOrders = realizedOrders;
	}

	/**
	 * Gets the canceled orders.
	 *
	 * @return the canceled orders
	 */
	public int getCanceledOrders() {
		return canceledOrders;
	}

	/**
	 * Sets the canceled orders.
	 *
	 * @param canceledOrders the new canceled orders
	 */
	public void setCanceledOrders(int canceledOrders) {
		this.canceledOrders = canceledOrders;
	}

	/**
	 * Gets the late arrivals.
	 *
	 * @return the late arrivals
	 */
	public int getLateArrivals() {
		return lateArrivals;
	}

	/**
	 * Sets the late arrivals.
	 *
	 * @param lateArrivals the new late arrivals
	 */
	public void setLateArrivals(int lateArrivals) {
		this.lateArrivals = lateArrivals;
	}

	/**
	 * Gets the complaints.
	 *
	 * @return the complaints
	 */
	public int getComplaints() {
		return complaints;
	}

	/**
	 * Sets the complaints.
	 *
	 * @param complaints the new complaints
	 */
	public void setComplaints(int complaints) {
		this.complaints = complaints;
	}
}