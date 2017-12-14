package cps.entities.people;

public class LocalWorker extends LocalEmployee {
	private static final long serialVersionUID = 1L;

	public LocalWorker(int id, String email, String username, String password, String firstName, String lastName,
			String jobTitle, int managerID, int departmentID) {
		super(id, email, username, password, firstName, lastName, jobTitle, managerID, departmentID);
		// TODO Auto-generated constructor stub
	}
}
