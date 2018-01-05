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

public class DailyStatistics implements Serializable {
	// `day` date NOT NULL,
	// `realized_orders` int(11) DEFAULT NULL,
	// `canceled_orders` int(11) DEFAULT NULL,
	// `late_arrivals` int(11) DEFAULT NULL,
	// `complaints` int(11) DEFAULT NULL,
	// PRIMARY KEY (`day`)

	private static final long serialVersionUID = 1L;

	private LocalDate day;
	private int realizedOrders;
	private int canceledOrders;
	private int lateArrivals;
	private int complaints;

	public DailyStatistics(LocalDate day, int realizedOrders, int canceledOrders, int lateArrivals, int complaints) {
		super();
		this.day = day;
		this.realizedOrders = realizedOrders;
		this.canceledOrders = canceledOrders;
		this.lateArrivals = lateArrivals;
		this.complaints = complaints;
	}

	public DailyStatistics(ResultSet rs) throws SQLException {
		this(rs.getDate(1).toLocalDate(), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));
	}

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

	public static void IncreaseRealizedOrder(Connection conn, int lotId) throws SQLException {
		IncreaseRealizedOrder(conn, LocalDate.now(), lotId);
	}

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
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public int getRealizedOrders() {
		return realizedOrders;
	}

	public void setRealizedOrders(int realizedOrders) {
		this.realizedOrders = realizedOrders;
	}

	public int getCanceledOrders() {
		return canceledOrders;
	}

	public void setCanceledOrders(int canceledOrders) {
		this.canceledOrders = canceledOrders;
	}

	public int getLateArrivals() {
		return lateArrivals;
	}

	public void setLateArrivals(int lateArrivals) {
		this.lateArrivals = lateArrivals;
	}

	public int getComplaints() {
		return complaints;
	}

	public void setComplaints(int complaints) {
		this.complaints = complaints;
	}
}