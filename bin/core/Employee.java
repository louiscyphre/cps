package cps.core;

public abstract class Employee extends CompanyPerson {

	private int managerID;
	private int departmentID;
	
	public Employee(int id, String email, String username, String password, String firstName, String lastName,
			String jobTitle, int managerID, int departmentID) {
		super(id, email, username, password, firstName, lastName, jobTitle);
		this.managerID = managerID;
		this.departmentID = departmentID;
	}

	public int getManagerID() {
		return managerID;
	}

	public void setManagerID(int managerID) {
		this.managerID = managerID;
	}

	public int getDepartmentID() {
		return departmentID;
	}

	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}
	
}
