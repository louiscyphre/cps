package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cps.common.Constants;

public class Complaint implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int customerID;
	private int employeeID;
	private int status;
	private String description;

	public Complaint(int id, int customerID, int employeeID, int status, String description) {
		this.id = id;
		this.customerID = customerID;
		this.employeeID = employeeID;
		this.status = status;
		this.description = description;
	}

	public Complaint (ResultSet rs) throws SQLException {
		this(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Complaint create(Connection conn, int customerID, int employeeID, int status, String description) throws SQLException {
		PreparedStatement st = conn.prepareStatement(Constants.SQL_CREATE_COMPLAINT,
				Statement.RETURN_GENERATED_KEYS);

		int field = 1;
		st.setInt(field++, customerID);
		st.setInt(field++, employeeID);
		st.setInt(field++, status);
		st.setString(field++, description);
		st.executeUpdate();

		ResultSet keys = st.getGeneratedKeys();
		int newID = 0;

		if (keys != null && keys.next()) {
			newID = keys.getInt(1);
			keys.close();
		}

		st.close();

		return new Complaint(newID, customerID, employeeID, status, description);
	}

	// Light update - write all fields except complaint description
	// Full update - write all fields, including description 
	public boolean update(Connection conn, boolean light) throws SQLException {
		String queryString = light ? Constants.SQL_UPDATE_COMPLAINT_LIGHT : Constants.SQL_UPDATE_COMPLAINT;
		PreparedStatement st = conn.prepareStatement(queryString);

		int field = 1;
		st.setInt(field++, customerID);
		st.setInt(field++, employeeID);
		st.setInt(field++, status);
		
		if (!light) {
			st.setString(field++, description);
		}

		int updated = st.executeUpdate();

		st.close();

		return updated > 0;	
	}
	
	// Default - full update
	public boolean update(Connection conn) throws SQLException {
		return update(conn, false);
	}

}
