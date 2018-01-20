package cps.server.testing.tests;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;

import org.junit.Test;

import cps.api.action.GetPeriodicReportAction;
import cps.api.response.PeriodicReportResponse;
import cps.api.response.ServerResponse;
import cps.entities.people.User;
import cps.server.session.ServiceSession;
import cps.server.testing.utilities.ServerControllerTest;

public class TestPeriodicReport extends ServerControllerTest {

  @Test
  public void testPeriodicReport() {
    header("testPeriodicReport");

    ServiceSession session = getContext().acquireServiceSession();

    User user = session.login("malki", "1234");
    assertTrue(user == session.getUser());
    assertNotNull(session.getUser());
    
    LocalDate start = LocalDate.now();
    LocalDate end = start.plusMonths(1);
    
    GetPeriodicReportAction action = new GetPeriodicReportAction(user.getId(), start, end);
    printObject(action);

    ServerResponse response = server.dispatch(action, getContext());
    assertNotNull(response);
    assertTrue(response.success());
    assertThat(response, instanceOf(PeriodicReportResponse.class));
    
    PeriodicReportResponse specificResponse = (PeriodicReportResponse) response;
    assertNotNull(specificResponse.getData());
    printObject(specificResponse.getData());
  }

}
