package cps.entities.people;

public interface User {
	int getId();
	String getEmail();
	int getUserType();
	int getAccessLevel();
	boolean canAccessDomain(int domain);
}
