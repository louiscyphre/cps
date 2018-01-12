package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import cps.common.Constants;
import cps.server.ServerException;

/**
 * The Class DailyStatistics.
 */
public class DailyStatistics implements Serializable {
	// `day` date NOT NULL,
	// `realized_orders` int(11) DEFAULT NULL,
	// `canceled_orders` int(11) DEFAULT NULL,
	// `late_arrivals` int(11) DEFAULT NULL,
	// `complaints` int(11) DEFAULT NULL,
	// `inactive_slots` int(10) DEFAULT '0',
	// PRIMARY KEY (`day`)

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The day. */
	private LocalDate day;

	/** The number of realized orders. */
	private int realizedOrders;

	/** The number of canceled orders. */
	private int canceledOrders;

	/** The number of late arrivals. */
	private int lateArrivals;

	/** The number of complaints. */
	private int complaints;
	
	/** The number of inactive slots. */
	private int inactiveSlots;

	/**
	 * Instantiates a new daily statistics.
	 *
	 * @param day
	 *            the day
	 * @param realizedOrders
	 *            the realized orders
	 * @param canceledOrders
	 *            the canceled orders
	 * @param lateArrivals
	 *            the late arrivals
	 * @param complaints
	 *            the complaints
	 */
	public DailyStatistics(LocalDate day, int realizedOrders, int canceledOrders, int lateArrivals, int complaints, int inactiveSlots) {
		super();
		this.day = day;
		this.realizedOrders = realizedOrders;
		this.canceledOrders = canceledOrders;
		this.lateArrivals = lateArrivals;
		this.complaints = complaints;
		this.setInactiveSlots(inactiveSlots);
	}

	/**
	 * Instantiates a new daily statistics.
	 *
	 * @param rs
	 *            Result Set
	 * @throws SQLException
	 *             the SQL exception
	 */
	public DailyStatistics(ResultSet rs) throws SQLException {
		this(rs.getDate(1).toLocalDate(), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6));
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
	 * @param day
	 *            the new day
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
	 * @param realizedOrders
	 *            the new realized orders
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
	 * @param canceledOrders
	 *            the new canceled orders
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
	 * @param lateArrivals
	 *            the new late arrivals
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
	 * @param complaints
	 *            the new complaints
	 */
	public void setComplaints(int complaints) {
		this.complaints = complaints;
	}

	public int getInactiveSlots() {
		return inactiveSlots;
	}

	public void setInactiveSlots(int inactiveSlots) {
		this.inactiveSlots = inactiveSlots;
	}

	/**
	 * Creates an empty entry in the table for specific date and lotId. All other
	 * parameters are zero by default
	 *
	 * @param conn
	 *            the conn
	 * @param today
	 *            the today
	 * @param lotId
	 *            the lot id
	 * @return the daily statistics
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static DailyStatistics create(Connection conn, LocalDate today, int lotId) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(Constants.SQL_CREATE_NEW_DAY);

		int field = 1;
		stmt.setDate(field++, Date.valueOf(today));
		stmt.setInt(field++, lotId);
		stmt.executeUpdate();

		stmt.close();
		return new DailyStatistics(today, lotId, 0, 0, 0, 0);

	}

	/**
	 * If statistics for set Date exists in database, the function returns the
	 * corresponding line as ResultSet. Else creates an empty line in database and
	 * returns null ResultSet
	 *
	 * @param conn
	 *            the connection
	 * @param _date
	 *            the date
	 * @param lotId
	 *            Id of the parking lot
	 * @return Result Set with given fields: Date day,int lot_id, int
	 *         realized_orders, int canceled_orders,int late_arrivals,int
	 *         inactive_slots.
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static DailyStatistics createIfNotExists(Connection conn, LocalDate _date, int lotId) throws SQLException {
		DailyStatistics item = null;
		
		PreparedStatement st = conn.prepareStatement(Constants.SQL_CHECK_DATE);
		st.setDate(1, Date.valueOf(_date));
		ResultSet rs = st.executeQuery();
		
		if (rs.wasNull()) {
			item = create(conn, _date, lotId);// if doesn't exists - create empty line with zeroes
		}

		rs.close();
		st.close();
		return item;
	}

	/**
	 * Increase realized order count by one for today in specific parking lot.
	 *
	 * @param conn
	 *            the conn
	 * @param lotId
	 *            the lot id
	 * @throws SQLException
	 *             the SQL exception
	 * @throws ServerException 
	 */
	public static void increaseRealizedOrder(Connection conn, int lotId) throws SQLException, ServerException {
		increaseRealizedOrder(conn, LocalDate.now(), lotId);
	}

	/**
	 * Increase realized order count by one in specific parking lot at specific
	 * date.
	 *
	 * @param conn
	 *            the conn
	 * @param _date
	 *            the date
	 * @param lotId
	 *            the lot id
	 * @throws SQLException
	 *             the SQL exception
	 * @throws ServerException 
	 */
	public static void increaseRealizedOrder(Connection conn, LocalDate _date, int lotId) throws SQLException, ServerException {
		// check if line exists in database
		DailyStatistics entry = createIfNotExists(conn, _date, lotId);
		
		if (entry != null) {
			int index = 1;
			PreparedStatement stmt = conn.prepareStatement(Constants.SQL_INCREASE_REALIZED_ORDER);
			stmt.setInt(index++, entry.getRealizedOrders() + 1); // get realized orders number and increase it
			stmt.setDate(index++, Date.valueOf(_date));
			stmt.setInt(index++, lotId);
			
			if (stmt.executeUpdate() == 0) {
				throw new ServerException("Failed to increase Realized Order Count");
			}
			
			stmt.close();
		}
	}

	/**
	 * Increase canceled order count by one for today in specific parking lot.
	 *
	 * @param conn
	 *            the conn
	 * @param lotId
	 *            the lot id
	 * @throws SQLException
	 *             the SQL exception
	 * @throws ServerException 
	 */
	public static void increaseCanceledOrder(Connection conn, int lotId) throws SQLException, ServerException {
		increaseCanceledOrder(conn, LocalDate.now(), lotId);
	}

	/**
	 * Increase canceled order count by one in specific parking lot at specific date.
	 *
	 * @param conn
	 *            the conn
	 * @param _date
	 *            the date
	 * @param lotId
	 *            the lot id
	 * @throws SQLException
	 *             the SQL exception
	 * @throws ServerException 
	 */
	public static void increaseCanceledOrder(Connection conn, LocalDate _date, int lotId) throws SQLException, ServerException {
		// check if line exists in database
		DailyStatistics entry = createIfNotExists(conn, _date, lotId);
		
		if (entry != null) {
			int index = 1;
			PreparedStatement stmt = conn.prepareStatement(Constants.SQL_INCREASE_CANCELED_ORDER);
			stmt.setInt(index++, entry.getCanceledOrders() + 1); // get canceled orders number and increase it
			stmt.setDate(index++, Date.valueOf(_date));
			stmt.setInt(index++, lotId);
			
			if (stmt.executeUpdate() == 0) {
				throw new ServerException("Failed to increase Canceled Order Count");
			}
			
			stmt.close();
		}
	}

	/**
	 * Increase Late Arrival count by one for today in specific parking lot.
	 *
	 * @param conn
	 *            the conn
	 * @param lotId
	 *            the lot id
	 * @throws SQLException
	 *             the SQL exception
	 * @throws ServerException 
	 */
	public static void increaseLateArrival(Connection conn, int lotId) throws SQLException, ServerException {
		increaseLateArrival(conn, LocalDate.now(), lotId);
	}

	/**
	 * Increase Late Arrival count by one in specific parking lot at specific date.
	 *
	 * @param conn
	 *            the conn
	 * @param _date
	 *            the date
	 * @param lotId
	 *            the lot id
	 * @throws SQLException
	 *             the SQL exception
	 * @throws ServerException 
	 */
	public static void increaseLateArrival(Connection conn, LocalDate _date, int lotId) throws SQLException, ServerException {		
		// check if line exists in database
		DailyStatistics entry = createIfNotExists(conn, _date, lotId);
		
		if (entry != null) {
			int index = 1;
			PreparedStatement stmt = conn.prepareStatement(Constants.SQL_INCREASE_LATE_ARRIVAL);
			stmt.setInt(index++, entry.getLateArrivals() + 1); // get late arrivals number and increase it
			stmt.setDate(index++, Date.valueOf(_date));
			stmt.setInt(index++, lotId);
			
			if (stmt.executeUpdate() == 0) {
				throw new ServerException("Failed to increase Late Arrival Count");
			}
			
			stmt.close();
		}
	}
	
	/**
	 * Increase Inactive Slots count by one for today in specific parking lot.
	 *
	 * @param conn
	 *            the conn
	 * @param lotId
	 *            the lot id
	 * @throws SQLException
	 *             the SQL exception
	 * @throws ServerException 
	 */
	public static void increaseInactiveSlots(Connection conn, int lotId) throws SQLException, ServerException {
		increaseInactiveSlots(conn, LocalDate.now(), lotId);
	}

	/**
	 * Increase Inactive Slots count by one in specific parking lot at specific date.
	 *
	 * @param conn
	 *            the conn
	 * @param _date
	 *            the date
	 * @param lotId
	 *            the lot id
	 * @throws SQLException
	 *             the SQL exception
	 * @throws ServerException 
	 */
	public static void increaseInactiveSlots(Connection conn, LocalDate _date, int lotId) throws SQLException, ServerException {	
		// check if line exists in database
		DailyStatistics entry = createIfNotExists(conn, _date, lotId);
		
		if (entry != null) {
			int index = 1;
			PreparedStatement stmt = conn.prepareStatement(Constants.SQL_INCREASE_INACTIVE_SLOTS);
			stmt.setInt(index++, entry.getInactiveSlots() + 1); // get number of inactive slots and increase it
			stmt.setDate(index++, Date.valueOf(_date));
			stmt.setInt(index++, lotId);
			
			if (stmt.executeUpdate() == 0) {
				throw new ServerException("Failed to increase Inactive Slot Count");
			}
			
			stmt.close();
		}
	}
}