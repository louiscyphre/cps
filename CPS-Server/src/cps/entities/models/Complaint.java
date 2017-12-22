package cps.entities.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Complaint extends Entity {
	private static final long serialVersionUID = 1L;
	private int id;
	private int customerId;
	private String description;
	private String status;
	private int employeeId;

	public Complaint(int id, int customerId, String description, String status, int employeeId) {
		this.id = id;
		this.customerId = customerId;
		this.description = description;
		this.status = status;
		this.employeeId = employeeId;
	}

	public static Complaint buildFromQueryResult(ResultSet rs) throws SQLException {
		return new Complaint(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
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

	public int getEmployee_id() {
		return employeeId;
	}

	public void setEmployee_id(int employee_id) {
		this.employeeId = employee_id;
	}

}
