package cps.server.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseController {
	String host;
	String dbName;
	String username;
	String password;
	
	public interface DatabaseAction {
		void perform(Connection conn) throws SQLException;
	}

	public DatabaseController(String host, String dbName, String username, String password) throws Exception {
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

	public void performAction(DatabaseAction action) {
		Connection conn = null;
		
		try {
			conn = getConnection();
			action.perform(conn);
		} catch (SQLException ex) {
			handleSQLException(ex);
		} finally {
			closeConnection(conn);
		}
	}
}