package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

import cps.common.Constants;

/**
 * The Class ParkingLot.
 */
public class ParkingLot implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Parking lot id. */
	private int id;

	/** Parking lot street address. */
	private String streetAddress;

	/** Amount of columns in parking lot. */
	private int size;

	/**
	 * Serialized three dimensional array that reflects status of the parking spots.
	 */
	private String content;

	/** The price of incidental parking. */
	private float price1;

	/** The price one time parking. */
	private float price2;

	/**
	 * Represents list of alternative parking lots that will be provided if the
	 * current lot is full.
	 */
	private String alternativeLots;

	/** IP address of the robot. */
	private String robotIP;

	private boolean lotFull;

	/**
	 * Instantiates a new parking lot.
	 *
	 * @param id
	 *            Parking lot id
	 * @param streetAddress
	 *            Parking lot street address
	 * @param size
	 *            Amount of columns
	 * @param content
	 *            Serialized three dimensional array that reflects status of the
	 *            parking spots
	 * @param price1
	 *            Price of incidental parking
	 * @param price2
	 *            Price of one time parking
	 * @param alternativeLots
	 *            List of alternative parking lots
	 * @param robotIP
	 *            IP address of the robot
	 */
	public ParkingLot(int id, String streetAddress, int size, String content, float price1, float price2,
			String alternativeLots, String robotIP, boolean lotfull) {
		this.id = id;
		this.streetAddress = streetAddress;
		this.size = size;
		this.content = content;
		this.price1 = price1;
		this.price2 = price2;
		this.alternativeLots = alternativeLots;
		this.robotIP = robotIP;
		this.lotFull = lotfull;
	}

	/**
	 * Instantiates a new parking lot.
	 *
	 * @param rs
	 *            the Result set
	 * @throws SQLException
	 *             the SQL exception
	 */
	public ParkingLot(ResultSet rs) throws SQLException {
		this(rs.getInt("id"), rs.getString("street_address"), rs.getInt("size"), rs.getString("content"),
				rs.getFloat("price1"), rs.getFloat("price2"), rs.getString("alternative_lots"),
				rs.getString("robot_ip"), rs.getBoolean("lot_full"));
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the street address.
	 *
	 * @return the street address
	 */
	public String getStreetAddress() {
		return streetAddress;
	}

	/**
	 * Sets the street address.
	 *
	 * @param streetAddress
	 *            the new street address
	 */
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the size.
	 *
	 * @param size
	 *            the new size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	public String[][][] getContentAsArray() {
		String[][][] result = new String[this.size][3][3];
		int iSize, iHeight, iDepth;
		String[] pSize, pHeight, pDepth;
		pSize = this.content.split("&&&");
		for (iSize = 0; iSize < this.size; iSize++) {
			pHeight = pSize[iSize].split("&&");
			for (iHeight = 0; iHeight < 3; iHeight++) {
				pDepth = pHeight[iHeight].split("&");
				for (iDepth = 0; iDepth < 3; iDepth++) {
					result[iSize][iHeight][iDepth] = pDepth[iDepth];
				}
			}
		}
		return result;
	}

	/**
	 * Sets the content.
	 *
	 * @param content
	 *            the new content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public void setContentFromArray(String[][][] newContent) {

		String result = "";
		int iSize, iHeight, iDepth;

		for (iSize = 0; iSize < this.size; iSize++) {
			for (iHeight = 0; iHeight < 3; iHeight++) {
				for (iDepth = 0; iDepth < 3; iDepth++) {
					result += newContent[iSize][iHeight][iDepth];
					result += "&";
				}
				result += "&";
			}
			result += "&";
		}
		result = result.substring(0, result.length() - 3);
		this.content = result;
	}

	/**
	 * Gets the price of incidental parking.
	 *
	 * @return the price of incidental parking
	 */
	public float getPrice1() {
		return price1;
	}

	/**
	 * Sets the price of incidental parking.
	 *
	 * @param price1
	 *            the new price of incidental parking
	 */
	public void setPrice1(float price1) {
		this.price1 = price1;
	}

	/**
	 * Gets the price of one time parking.
	 *
	 * @return the price of one time parking
	 */
	public float getPrice2() {
		return price2;
	}

	public float getPriceForService(int serviceType) {
		switch (serviceType) {
		case Constants.PARKING_TYPE_INCIDENTAL:
			return price1;
		default:
			return price2;
		}
	}

	/**
	 * Sets the price of one time parking.
	 *
	 * @param price2
	 *            the new price of one time parking
	 */
	public void setPrice2(float price2) {
		this.price2 = price2;
	}

	/**
	 * Gets the alternative lots.
	 *
	 * @return the alternative lots
	 */
	public String getAlternativeLots() {
		return alternativeLots;
	}

	/**
	 * Sets the alternative lots.
	 *
	 * @param alternativeLots
	 *            the new alternative lots
	 */
	public void setAlternativeLots(String alternativeLots) {
		this.alternativeLots = alternativeLots;
	}

	/**
	 * Gets the robot IP.
	 *
	 * @return the robot IP
	 */
	public String getRobotIP() {
		return robotIP;
	}

	/**
	 * Sets the robot IP.
	 *
	 * @param robotIP
	 *            the new robot IP
	 */
	public void setRobotIP(String robotIP) {
		this.robotIP = robotIP;
	}

	public boolean isLotFull() {
		return lotFull;
	}

	public void setLotFull(boolean lotFull) {
		this.lotFull = lotFull;
	}

	/**
	 * Insert new parking lot into the database and create a new parking lot object.
	 *
	 * @param conn
	 *            Connection to database server
	 * @param streetAddress
	 *            Parking lot street address
	 * @param size
	 *            Amount of columns in parking lot
	 * @param price1
	 *            The price of incidental parking
	 * @param price2
	 *            The price one time parking
	 * @param robotIP
	 *            IP address of the robot
	 * @return New parking lot object
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static ParkingLot create(Connection conn, String streetAddress, int size, float price1, float price2,
			String robotIP) throws SQLException {
		// Create SQL statement and request table for table keys
		PreparedStatement stmt = conn.prepareStatement(Constants.SQL_CREATE_PARKING_LOT,
				Statement.RETURN_GENERATED_KEYS);
		String lotContent = generateEmptyContent(size);
		// Fill in the fields of SQL statement
		int field = 1;
		stmt.setString(field++, streetAddress);
		stmt.setInt(field++, size);
		// Parking lot content as a string
		stmt.setString(field++, lotContent);
		stmt.setFloat(field++, price1);
		stmt.setFloat(field++, price2);
		stmt.setString(field++, robotIP);
		// Execute SQL query
		stmt.executeUpdate();
		// Extract the auto-generated keys created as a result of executing this
		// Statement object
		ResultSet keys = stmt.getGeneratedKeys();
		int newID = 0;
		// if keys were created take the first one
		// A ResultSet cursor is initially positioned before the first row; the first
		// call to the method next makes the first row the current row
		if (keys != null && keys.next()) {
			newID = keys.getInt(1);
			keys.close();
		}

		stmt.close();

		return new ParkingLot(newID, streetAddress, size, lotContent, price1, price2, "", robotIP, false);
	}

	private static String generateEmptyContent(int size) {
		String result = "";
		int iSize, iHeight, iDepth;

		for (iSize = 0; iSize < size; iSize++) {
			for (iHeight = 0; iHeight < 3; iHeight++) {
				for (iDepth = 0; iDepth < 3; iDepth++) {
					result += Constants.SPOT_IS_EMPTY;
					result += "&";
				}
				result += "&";
			}
			result += "&";
		}
		result = result.substring(0, result.length() - 3);
		return result;
	}

	/**
	 * Find by ID.
	 *
	 * @param conn
	 *            the conn
	 * @param id
	 *            the id
	 * @return the parking lot
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static ParkingLot findByID(Connection conn, int id) throws SQLException {
		ParkingLot result = null;

		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM parking_lot WHERE id = ?");
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			result = new ParkingLot(rs);
		}

		rs.close();
		stmt.close();

		return result;
	}

	public static ParkingLot findByIDNotNull(Connection conn, int id) throws SQLException, DatabaseException {
		ParkingLot result = findByID(conn, id);

		if (result == null) {
			throw new DatabaseException("ParkingLot with id " + id + " does not exist");
		}

		return result;
	}

	public int getFreeSpotsNumber() {
		int iSize, iHeight, iDepth;
		int free = 0;
		String[][][] content = this.getContentAsArray();

		for (iSize = 0; iSize < this.size; iSize++) {
			for (iHeight = 0; iHeight < 3; iHeight++) {
				for (iDepth = 0; iDepth < 3; iDepth++) {
					if ((content[iSize][iHeight][iDepth]).equals(Constants.SPOT_IS_EMPTY)) {
						free++;
					}
				}
			}
		}
		return free;
	}

	public int update(Connection conn) throws SQLException, DatabaseException {
		java.sql.PreparedStatement st = conn.prepareStatement(Constants.SQL_UPDATE_PARKING_LOT);
		int index = 1;
		int result = 0;
		st.setString(index++, this.streetAddress);
		st.setInt(index++, this.size);
		st.setString(index++, this.content);
		st.setFloat(index++, this.price1);
		st.setFloat(index++, this.price2);
		st.setString(index++, this.alternativeLots);
		st.setString(index++, this.robotIP);
		st.setBoolean(index++, this.lotFull);
		st.setInt(index++, this.id);
		result = st.executeUpdate();
		st.close();
		
		if (result <= 0) {
			throw new DatabaseException("Failed to update ParkingLot");
		}
		
		return result;
	}

	public static Collection<ParkingLot> findAll(Connection conn) throws SQLException {
		LinkedList<ParkingLot> results = new LinkedList<ParkingLot>();

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(Constants.SQL_FIND_ALL_PARKING_LOTS);

		while (rs.next()) {
			results.add(new ParkingLot(rs));
		}

		rs.close();
		stmt.close();

		return results;
	}
}
