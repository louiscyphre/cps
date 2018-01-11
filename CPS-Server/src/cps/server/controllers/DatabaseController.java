/*
 * 
 */
package cps.server.controllers;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;

import cps.api.response.ServerResponse;
import cps.server.ServerException;

/**
 * The Class DatabaseController.
 */
public class DatabaseController {
	
	/** The host address. */
	String host;
	
	/** Database name. */
	String dbName;
	
	/** Username. */
	String username;
	
	/** Password. */
	String password;
	
	/**
	 * The Interface DatabaseAction.
	 */
	public interface DatabaseAction {
		
		/**
		 * Perform.
		 *
		 * @param conn the SQL connection
		 * @throws SQLException the SQL exception
		 */
		void perform(Connection conn) throws SQLException;
	}
	
	public interface DatabaseQuery<T> {
		
		/**
		 * Perform.
		 *
		 * @param conn the SQL connection
		 * @throws SQLException the SQL exception
		 */
		T perform(Connection conn) throws SQLException;
	}
	
	public interface DatabaseQueryWithException<T> {
		
		/**
		 * Perform.
		 *
		 * @param conn the SQL connection
		 * @throws SQLException the SQL exception
		 */
		T perform(Connection conn, T response) throws SQLException, ServerException;
	}
	
	public interface EntityBuilder<T> {
		T fromResultSet(ResultSet result) throws SQLException;
	}

	/**
	 * Instantiates a new database controller.
	 *
	 * @param host the database host address
	 * @param dbName the database name
	 * @param username the user name
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
		ex.printStackTrace();
	}

	/**
	 * Close connection.
	 *
	 * @param conn the SQL connection
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
	
	public <T> T performQuery(DatabaseQuery<T> query) {
		Connection conn = null;
		T result = null;
		
		try {
			conn = getConnection();
			result = query.perform(conn);
		} catch (SQLException ex) {
			handleSQLException(ex);
		} finally {
			closeConnection(conn);
		}
		
		return result;
	}
	
	public <T extends ServerResponse> T performQuery(T response, DatabaseQueryWithException<T> query) {
		Connection conn = null;
		T result = null;
		
		try {
			conn = getConnection();
			result = query.perform(conn, response);
		} catch (SQLException ex) {
			handleSQLException(ex);
		} catch (ServerException ex) {
			response.setError(ex.getMessage());
			return response;
		} finally {
			// This still gets called even if the previous block returns
			closeConnection(conn);
		}
		
		return result;
	}

	public void truncateTables() {		
		performAction(conn -> {
			Collection<String> tables = getTables(conn);
			
			for (String table : tables) {
//			    System.out.println("TRUNCATE " + table);
			    Statement stmt = conn.createStatement();
			    stmt.executeUpdate("TRUNCATE " + table);
			    stmt.close();
			}
		});
	}
	
	public Collection<String> getTables(Connection conn) throws SQLException {
		LinkedList<String> results = new LinkedList<>();
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SHOW TABLES");
		
		while (rs.next()) {
			results.add(rs.getString(1));
		}
		
		rs.close();
		stmt.close();
		
		return results;
	}
	
	public Collection<String> getTables() {		
		return performQuery(conn -> getTables(conn));
	}
	
	public int countEntities(Connection conn, String table) throws SQLException {		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + table);
		
		int count = 0;
		
		if (rs.next()) {
			count = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		
		return count;
		
	}
	
	public int countEntities(String table) {		
		return performQuery(conn -> countEntities(conn, table));
	}
	
	public <T> Collection<T> findAll(Connection conn, String table, String orderBy, EntityBuilder<T> builder) throws SQLException {
		LinkedList<T> results = new LinkedList<T>();

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM %s ORDER BY %s", table, orderBy));

		while (rs.next()) {
			results.add(builder.fromResultSet(rs));
		}

		rs.close();
		stmt.close();

		return results;
	}
}
