package cps.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;

import cps.common.Constants;

// Database entity for one-time parking services - incidental parking or reserved parking both stored in the same table.

public class OnetimeParking implements Serializable {
	private static final long serialVersionUID = 1L;
	public int id;
	public int type; // 1 = incidental, 2 = reserved
	public int customerID;
	public String email;
	public String carID;
	public int lotID;
	public Timestamp plannedStartTime; // null for incidental
	public Timestamp plannedEndTime;
	public Timestamp actualStartTime;
	public Timestamp actualEndTime;

	public OnetimeParking(int id, int type, int customerID, String email, String carID, int lotID,
			Timestamp plannedStartTime, Timestamp plannedEndTime, Timestamp actualStartTime, Timestamp actualEndTime) {
		this.id = id;
		this.type = type;
		this.customerID = customerID;
		this.email = email;
		this.carID = carID;
		this.lotID = lotID;
		this.plannedStartTime = plannedStartTime;
		this.plannedEndTime = plannedEndTime;
		this.actualStartTime = actualStartTime;
		this.actualEndTime = actualEndTime;
	}

	public static OnetimeParking buildFromQueryResult(ResultSet rs) throws SQLException {
		return new OnetimeParking(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getInt(6),
				rs.getTimestamp(7), rs.getTimestamp(8), rs.getTimestamp(9), rs.getTimestamp(10));
	}

	public static OnetimeParking create(Connection conn, int type, int customerID, String email, String carID, int lotID,
			Timestamp plannedStartTime, Timestamp plannedEndTime, Timestamp actualStartTime, Timestamp actualEndTime)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(Constants.SQL_CREATE_ONETIME_PARKING,
				Statement.RETURN_GENERATED_KEYS);

		int field = 1;
		stmt.setInt(field++, type);
		stmt.setInt(field++, customerID);
		stmt.setString(field++, email);
		stmt.setString(field++, carID);
		stmt.setInt(field++, lotID);
		stmt.setTimestamp(field++, plannedStartTime);
		stmt.setTimestamp(field++, plannedEndTime);
		stmt.setTimestamp(field++, actualStartTime);
		stmt.setTimestamp(field++, actualEndTime);
		stmt.executeUpdate();

		ResultSet keys = stmt.getGeneratedKeys();
		int newID = 0;

		if (keys != null && keys.next()) {
			newID = keys.getInt(1);
			keys.close();
		}

		//conn.commit();
		stmt.close();

		return new OnetimeParking(newID, type, customerID, email, carID, lotID, plannedStartTime, plannedEndTime,
				actualStartTime, actualEndTime);
	}

	public static Collection<OnetimeParking> findByCustomerID(Connection conn, int userID) throws SQLException {
	    LinkedList<OnetimeParking> results = new LinkedList<OnetimeParking>();
	    
	    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM OnetimeParking WHERE customer_id = ? ORDER BY id");
	    stmt.setInt(1, userID);
	    ResultSet rs = stmt.executeQuery();
	    
	    while (rs.next()) {
	      results.add(OnetimeParking.buildFromQueryResult(rs));
	    }
	    
	    rs.close();
	    stmt.close();
	    
	    return results;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("{");
		buffer.append("id: " + id + ", ");
		buffer.append("type: " + type + ", ");
		buffer.append("customerID: " + customerID + ", ");
		buffer.append("email: " + email + ", ");
		buffer.append("carID: " + carID + ", ");
		buffer.append("lotID: " + lotID + ", ");
		buffer.append("plannedStartTime: " + plannedStartTime + ", ");
		buffer.append("plannedEndTime: " + plannedEndTime + ", ");
		buffer.append("actualStartTime: " + actualStartTime + ", ");
		buffer.append("actualEndTime: " + actualEndTime + "}");
		return buffer.toString();
	}
}
