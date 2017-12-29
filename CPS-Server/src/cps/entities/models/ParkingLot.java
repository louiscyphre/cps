package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cps.common.Constants;

public class ParkingLot implements Serializable {
	private static final long serialVersionUID = 1L;
	// `id` int(11) NOT NULL AUTO_INCREMENT,
	// `street_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
	// `size` int(11) DEFAULT NULL,
	// `content` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
	// `price1` float DEFAULT NULL,
	// `price2` float DEFAULT NULL,
	// `alternative_lots` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,

	private int id;
	private String streetAddress;
	private int size;
	private String content;
	private float price1;
	private float price2;
	private String alternativeLots;
	private String robotIP;

	public ParkingLot(int id, String streetAddress, int size, String content, float price1, float price2,
			String alternativeLots, String robotIP) {
		this.id = id;
		this.streetAddress = streetAddress;
		this.size = size;
		this.content = content;
		this.price1 = price1;
		this.price2 = price2;
		this.alternativeLots = alternativeLots;
		this.robotIP = robotIP;
	}

	public ParkingLot(ResultSet rs) throws SQLException {
		this(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getFloat(5), rs.getFloat(6),
				rs.getString(7), rs.getString(8));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public float getPrice1() {
		return price1;
	}

	public void setPrice1(float price1) {
		this.price1 = price1;
	}

	public float getPrice2() {
		return price2;
	}

	public void setPrice2(float price2) {
		this.price2 = price2;
	}

	public String getAlternativeLots() {
		return alternativeLots;
	}

	public void setAlternativeLots(String alternativeLots) {
		this.alternativeLots = alternativeLots;
	}

	public String getRobotIP() {
		return robotIP;
	}

	public void setRobotIP(String robotIP) {
		this.robotIP = robotIP;
	}

	public static ParkingLot create(Connection conn, String streetAddress, int size, float price1, float price2,
			String robotIP) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(Constants.SQL_CREATE_PARKING_LOT,
				Statement.RETURN_GENERATED_KEYS);

		int field = 1;
		stmt.setString(field++, streetAddress);
		stmt.setInt(field++, size);
		stmt.setFloat(field++, price1);
		stmt.setFloat(field++, price2);
		stmt.setString(field++, robotIP);
		stmt.executeUpdate();

		ResultSet keys = stmt.getGeneratedKeys();
		int newID = 0;

		if (keys != null && keys.next()) {
			newID = keys.getInt(1);
			keys.close();
		}

		// conn.commit();
		stmt.close();

		return new ParkingLot(newID, streetAddress, size, "", price1, price2, "", robotIP);
	}

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
}
