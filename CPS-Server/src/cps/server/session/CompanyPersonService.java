package cps.server.session;

import cps.entities.people.*;

public class CompanyPersonService {

  private static final CompanyPerson[] people = {
      new GlobalManager(1, "ceo@cps.com", "malki", "1234", "Abigail", "Williams", "GlobalManager"),
      new LocalManager(2, "theo@cps.com", "theo", "1234", "Theodor", "Herzl", "LocalManager", 1, 1),
      new LocalManager(3, "george@cps.com", "george", "1234", "George", "V the Brilliant", "LocalManager", 1, 2),
      new LocalManager(4, "gordon@cps.com", "gordon", "1234", "Gordon", "Freeman", "LocalManager", 1, 3),
      new LocalWorker(5, "sarit@cps.com", "sarit", "1234", "Sarit", "Hadad", "LocalEmployee", 2, 1),
      new LocalWorker(6, "ghazan@cps.com", "ghazan", "1234", "Mahmud", "Ghazan", "LocalEmployee", 3, 2),
      new LocalWorker(7, "alyx@cps.com", "alyx", "1234", "Alyx", "Vance", "LocalEmployee", 4, 3),
      new CustomerServiceWorker(8, "eli@cps.com", "eli", "1234", "Elizabeth", "Bathory", "CustomerServiceWorker", 1,
          1000) };

  public static CompanyPerson findWithLoginData(String username, String password) {
    for (CompanyPerson person : people) {
      if (person.getUsername().equals(username) && person.getPassword().equals(password)) {
        return person;
      }
    }

    return null;
  }

  public static int getNumberOfEmployees() {
    return people.length;
  }
  
  public static CompanyPerson getEmployee(int index) {
    return people[index];
  }

}
