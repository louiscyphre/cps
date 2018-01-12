package cps.entities.people;

public class GlobalManager extends CompanyPerson {
	private static final long serialVersionUID = 1L;

	public GlobalManager(int id, String email, String username, String password, String firstName, String lastName,
			String jobTitle) {
		super(id, email, username, password, firstName, lastName, jobTitle);
	}

	public void requestStatistics() {
		throw new UnsupportedOperationException();
	}
}
