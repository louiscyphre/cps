/*
 * 
 */
package cps.server.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// TODO: Auto-generated Javadoc
/**
 * The Class DatabaseController.
 */
public class DatabaseController {
	
	/** The host. */
	String host;
	
	/** The db name. */
	String dbName;
	
	/** The username. */
	String username;
	
	/** The password. */
	String password;
	
	/**
	 * The Interface DatabaseAction.
	 */
	public interface DatabaseAction {
		
		/**
		 * Perform.
		 *
		 * @param conn the conn
		 * @throws SQLException the SQL exception
		 */
		void perform(Connection conn) throws SQLException;
	}

	/**
	 * Instantiates a new database controller.
	 *
	 * @param host the host
	 * @param dbName the db name
	 * @param username the username
	 * @param password the password
	 * @throws Exception the exception
	 */
	public DatabaseController(String host, String dbName, String username, String password) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		this.host = host;
		this.dbName = dbName;
		this.username = username;
		this.password = password;
	}

	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 * @throws SQLException the SQL exception
	 */
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://" + host + "/" + dbName, username, password);
	}

	/**
	 * Handle SQL exception.
	 *
	 * @param ex the exception
	 */
	public void handleSQLException(SQLException ex) {
		System.out.println("SQLException: " + ex.getMessage());
		System.out.println("SQLState: " + ex.getSQLState());
		System.out.println("VendorError: " + ex.getErrorCode());
	}

	/**
	 * Close connection.
	 *
	 * @param conn the connection
	 */
	public void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Perform action.
	 *
	 * @param action the action
	 */
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
