package cps.entities.people;

/** Bridges between Customers and System Users (Subclasses of CompanyPerson). */
public interface User {
  int getId();

  String getEmail();

  int getUserType();

  int getAccessLevel();

  boolean canAccessDomain(int domain);
}
