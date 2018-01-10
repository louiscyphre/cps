package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import cps.common.Constants;

public class Complaint implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int customerID;
	private int employeeID;
	private int status;
	private String description;
	private Timestamp createdAt;
	private Timestamp resolvedAt;

	public Complaint(int id, int customerID, int employeeID, int status, String description, Timestamp createdAt,
			Timestamp resolvedAt) {
		this.id = id;
		this.customerID = customerID;
		this.employeeID = employeeID;
		this.status = status;
		this.description = description;
		this.createdAt = createdAt;
		this.resolvedAt = resolvedAt;
	}

	public Complaint(ResultSet rs) throws SQLException {
		this(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
				rs.getTimestamp(6), rs.getTimestamp(7));
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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getResolvedAt() {
		return resolvedAt;
	}

	public void setResolvedAt(Timestamp resolvedAt) {
		this.resolvedAt = resolvedAt;
	}

	public static Complaint create(Connection conn, int customerID, String description, Timestamp createdAt, Timestamp resolvedAt) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(Constants.SQL_CREATE_COMPLAINT,
				Statement.RETURN_GENERATED_KEYS);

		int field = 1;
		int status = Constants.COMPLAINT_STATUS_PROCESSING;
		statement.setInt(field++, customerID);
		statement.setInt(field++, status);
		statement.setString(field++, description);
		statement.setTimestamp(field++, createdAt);
		statement.setTimestamp(field++, resolvedAt);
		statement.executeUpdate();

		ResultSet keys = statement.getGeneratedKeys();
		int newID = 0;

		if (keys != null && keys.next()) {
			newID = keys.getInt(1);
			keys.close();
		}

		statement.close();

		return new Complaint(newID, customerID, 0, status, description, createdAt, resolvedAt);
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
