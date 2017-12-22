package cps.entities.models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Complaint implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int customerID;
	private String description;
	private String status;
	private int employeeID;

	public Complaint(int id, int customerID, String description, String status, int employeeID) {
		this.id = id;
		this.customerID = customerID;
		this.description = description;
		this.status = status;
		this.employeeID = employeeID;
	}

	public Complaint (ResultSet rs) throws SQLException {
		this(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5));
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
