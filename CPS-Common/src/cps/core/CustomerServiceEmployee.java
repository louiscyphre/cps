package cps.core;

public abstract class CustomerServiceEmployee extends Employee {
	private static final long serialVersionUID = 1L;

	public CustomerServiceEmployee(int id, String email, String username, String password, String firstName,
			String lastName, String jobTitle, int managerID, int departmentID) {
		super(id, email, username, password, firstName, lastName, jobTitle, managerID, departmentID);
		// TODO Auto-generated constructor stub
	}

	public void receiveComplaint() {
		throw new UnsupportedOperationException();
	}

	public void authorizeRefund() {
		throw new UnsupportedOperationException();
	}

}
