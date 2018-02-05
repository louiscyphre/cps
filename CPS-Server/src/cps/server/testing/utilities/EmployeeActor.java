package cps.server.testing.utilities;

import static org.junit.Assert.assertNotNull;

import cps.entities.people.CompanyPerson;
import cps.entities.people.User;
import cps.server.session.CompanyPersonService;
import cps.server.session.ServiceSession;
import cps.server.session.SessionHolder;

public class EmployeeActor implements Actor {
  SessionHolder context = new SessionHolder();
  int           token;

  enum State {
    INITIAL, READY
  };

  State state;

  public EmployeeActor(World world, int token) {
    this.token = token;
    this.state = State.INITIAL;
  }

  @Override
  public void act(World world) {
    switch (state) {
    case INITIAL:
      actInitial(world);
      break;
    default:
      break;
    }
  }

  private void actInitial(World world) {
    CompanyPerson person = CompanyPersonService.getEmployee(token - 1);
    ServiceSession session = context.acquireServiceSession();
    User user = session.login(person.getUsername(), person.getPassword());
    assertNotNull(user);
    state = State.READY;
  }

}
