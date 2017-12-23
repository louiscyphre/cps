package cps.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import cps.api.request.IncidentalParkingRequest;
import cps.entities.models.OnetimeService;

public class DatabaseController {
	String host;
	String dbName;
	String username;
	String password;

	DatabaseController(String host, String dbName, String username, String password) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		this.host = host;
		this.dbName = dbName;
		this.username = username;
		this.password = password;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://" + host + "/" + dbName, username, password);
	}

	public void handleSQLException(SQLException ex) {
		System.out.println("SQLException: " + ex.getMessage());
		System.out.println("SQLState: " + ex.getSQLState());
		System.out.println("VendorError: " + ex.getErrorCode());
	}

	public void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public OnetimeService insertIncidentalParking(IncidentalParkingRequest incidentalParking) {
		Connection conn = null;
		OnetimeService onetimeParking = null;

		try {
			conn = getConnection();
			Timestamp startTime = new Timestamp(System.currentTimeMillis());
			Timestamp plannedEndTime = Timestamp.valueOf(incidentalParking.getPlannedEndTime());
			onetimeParking = OnetimeService.create(conn, IncidentalParkingRequest.TYPE, incidentalParking.getCustomerID(),
					incidentalParking.getEmail(), incidentalParking.getCarID(), incidentalParking.getLotID(), startTime,
					plannedEndTime, false);
			// System.out.println(onetimeParking);
		} catch (SQLException ex) {
			handleSQLException(ex);
		} finally {
			closeConnection(conn);
		}

		return onetimeParking;
	}

	public Collection<OnetimeService> getOnetimeParkingEntriesForCustomer(int customerID) {
		Connection conn = null;
		Collection<OnetimeService> results = null;
		
		try {
			conn = getConnection();
			results = OnetimeService.findByCustomerID(conn, customerID);
		} catch (SQLException ex) {
			handleSQLException(ex);
		} finally {
			closeConnection(conn);
		}
		
		return results;
	}
}
